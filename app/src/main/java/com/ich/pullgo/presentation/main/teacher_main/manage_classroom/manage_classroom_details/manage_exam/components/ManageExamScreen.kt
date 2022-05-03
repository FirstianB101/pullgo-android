package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ExamItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ManageExamScreen(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()

    val createExamDialogState = remember { mutableStateOf(false) }
    val manageExamDialogState = remember { mutableStateOf(false) }
    val deleteExamDialogState = remember { mutableStateOf(false) }

    var spinnerExpanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("모든 시험")}
    val options = listOf("모든 시험","종료된 시험","취소된 시험")

    LaunchedEffect(Unit){
        viewModel.setClassroomId(selectedClassroom.id!!)
        viewModel.onEvent(ManageClassroomManageExamEvent.GetAllExams)
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManageClassroomManageExamViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ManageClassroomManageExamViewModel.UiEvent.RefreshOption -> {
                    sortOption = "모든 시험"
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
                                "모든 시험" -> viewModel.onEvent(ManageClassroomManageExamEvent.GetAllExams)
                                "종료된 시험" -> viewModel.onEvent(ManageClassroomManageExamEvent.GetFinishedExams)
                                "취소된 시험" -> viewModel.onEvent(ManageClassroomManageExamEvent.GetCancelledExams)
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
            items(state.value.exams.size){ idx ->
                ExamItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            viewModel.onEvent(ManageClassroomManageExamEvent.SelectExam(state.value.exams[idx]))
                            manageExamDialogState.value = true
                        },
                    exam = state.value.exams[idx],
                    showDeleteButton = true
                ) {
                    viewModel.onEvent(ManageClassroomManageExamEvent.SelectExam(state.value.exams[idx]))
                    deleteExamDialogState.value = true
                }
            }
        }
        if(state.value.exams.isEmpty()){
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                createExamDialogState.value = true
            },
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    TwoButtonDialog(
        title = "시험 삭제",
        content = "${state.value.selectedExam?.name} 시험을 삭제하시겠습니까?",
        dialogState = deleteExamDialogState,
        cancelText = "취소",
        confirmText = "삭제",
        onCancel = { deleteExamDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageClassroomManageExamEvent.DeleteExam)
            deleteExamDialogState.value = false
        }
    )

    CreateExamDialog(
        selectedClassroomId = selectedClassroom.id!!,
        showDialog = createExamDialogState
    ){
        viewModel.onEvent(ManageClassroomManageExamEvent.CreateExam(it))
        createExamDialogState.value = false
    }

    if(state.value.selectedExam != null) {
        ManageExamDialog(
            selectedExam = state.value.selectedExam!!,
            viewModel = viewModel,
            showDialog = manageExamDialogState
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}