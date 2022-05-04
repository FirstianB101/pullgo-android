package com.ich.pullgo.presentation.main.student_main.exam_list_screen.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ExamItem
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.StudentExamListEvent
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.StudentExamListViewModel
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam.TakeExamActivity
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentExamListScreen(
    listViewModel: StudentExamListViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = listViewModel.state.collectAsState()

    var spinnerExpanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("이름 순") }
    val options = listOf(
        stringResource(R.string.sort_exam_by_name),
        stringResource(R.string.sort_exam_by_begin_date),
        stringResource(R.string.sort_exam_by_end_date)
    )

    val takeDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        listViewModel.eventFlow.collectLatest { event ->
            when(event){
                is StudentExamListViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is StudentExamListViewModel.UiEvent.StartTakingExam -> {
                    val intent = Intent(context, TakeExamActivity::class.java).apply {
                        putExtra("selectedExam",state.value.selectedExam)
                        putExtra("selectedState",state.value.examState)
                    }
                    context.startActivity(intent)
                    sortOption = "이름 순"
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(8.dp),
            expanded = spinnerExpanded,
            onExpandedChange = {
                spinnerExpanded = !spinnerExpanded
            }
        ) {
            TextButton(
                onClick = {
                    spinnerExpanded = true
                }
            ){
                Text(
                    text = sortOption,
                    color = Color.Black
                )
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = spinnerExpanded
                )
            }
            ExposedDropdownMenu(
                expanded = spinnerExpanded,
                onDismissRequest = {
                    spinnerExpanded = false
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            sortOption = option
                            spinnerExpanded = false

                            when(option){
                                "이름 순" -> listViewModel.onEvent(StudentExamListEvent.GetExamsByName)
                                "시작날짜 순" -> listViewModel.onEvent(StudentExamListEvent.GetExamsByBeginDate)
                                "종료날짜 순" -> listViewModel.onEvent(StudentExamListEvent.GetExamsByEndDate)
                            }
                        }
                    ) {
                        Text(
                            text = option,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        LazyColumn{
            items(state.value.filteredExams.size){ idx ->
                ExamItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            listViewModel.onEvent(StudentExamListEvent.SelectExam(state.value.filteredExams[idx]))
                            takeDialogState.value = true
                        },
                    exam = state.value.filteredExams[idx],
                    showDeleteButton = false,
                    onDeleteButtonClicked = {}
                )
            }
        }
        if(state.value.filteredExams.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "시험이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

    TwoButtonDialog(
        title = stringResource(R.string.take_exam),
        content = "${state.value.selectedExam?.name} 시험을 응시하시겠습니까?",
        dialogState = takeDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.do_take_exam),
        onCancel = { takeDialogState.value = false },
        onConfirm = {
            listViewModel.onEvent(StudentExamListEvent.StartTakingExam)
            takeDialogState.value = false
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}