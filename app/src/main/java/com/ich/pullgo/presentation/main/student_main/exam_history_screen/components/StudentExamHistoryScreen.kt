package com.ich.pullgo.presentation.main.student_main.exam_history_screen.components

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
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ExamItem
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.StudentExamHistoryEvent
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.StudentExamHistoryViewModel
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review.ExamReviewActivity
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.StudentExamListState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentExamHistoryScreen(
    viewModel: StudentExamHistoryViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    var spinnerExpanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("이름 순") }
    val options = listOf(
        stringResource(R.string.sort_exam_by_name),
        stringResource(R.string.sort_exam_by_begin_date),
        stringResource(R.string.sort_exam_by_end_date)
    )

    val showDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is StudentExamHistoryViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is StudentExamHistoryViewModel.UiEvent.StartReviewing -> {
                    val intent = Intent(context, ExamReviewActivity::class.java).apply {
                        putExtra("selectedExam",state.value.selectedExam)
                        putExtra("attenderState",event.attenderState)
                    }
                    context.startActivity(intent)
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
                                "이름 순" -> viewModel.onEvent(StudentExamHistoryEvent.GetExamsByName)
                                "시작날짜 순" -> viewModel.onEvent(StudentExamHistoryEvent.GetExamsByBeginDate)
                                "종료날짜 순" -> viewModel.onEvent(StudentExamHistoryEvent.GetExamsByEndDate)
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
            items(state.value.takenExams.size){ idx ->
                ExamItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            viewModel.onEvent(StudentExamHistoryEvent.SelectExam(state.value.takenExams[idx]))
                            showDialogState.value = true
                        },
                    exam = state.value.takenExams[idx],
                    showDeleteButton = false,
                    onDeleteButtonClicked = {}
                )
            }
        }
        if(state.value.takenExams.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "응시한 시험이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

    TwoButtonDialog(
        title = stringResource(R.string.taken_exam),
        content = "${state.value.selectedExam?.name} 시험을 확인하시겠습니까?",
        dialogState = showDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.show_history),
        onCancel = { showDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(StudentExamHistoryEvent.StartReviewing)
            showDialogState.value = false
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