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
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ExamItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ManageExamScreen(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val exams: MutableState<List<Exam?>> = remember { mutableStateOf(listOf(null)) }
    var selectedExam: Exam? by remember{ mutableStateOf(null) }

    val createExamDialogState = remember { mutableStateOf(false) }
    val manageExamDialogState = remember { mutableStateOf(false) }
    val deleteExamDialogState = remember { mutableStateOf(false) }

    var spinnerExpanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("모든 시험")}
    val options = listOf("모든 시험","종료된 시험","취소된 시험")

    LaunchedEffect(Unit){
        viewModel.getExamsInClassroom(selectedClassroom.id!!)
    }

    fun refreshExamList(){
        sortOption = "모든 시험"
        viewModel.getExamsInClassroom(selectedClassroom.id!!)
    }

    when(state.value){
        is ManageClassroomManageExamState.GetExams -> {
            exams.value = (state.value as ManageClassroomManageExamState.GetExams).exams
            viewModel.onResultConsume()
        }
        is ManageClassroomManageExamState.CreateExam -> {
            Toast.makeText(context,"시험이 생성되었습니다",Toast.LENGTH_SHORT).show()
            refreshExamList()
        }
        is ManageClassroomManageExamState.DeleteExam -> {
            Toast.makeText(context,"시험이 삭제되었습니다",Toast.LENGTH_SHORT).show()
            refreshExamList()
        }
        is ManageClassroomManageExamState.EditExam,
        is ManageClassroomManageExamState.CancelExam,
        is ManageClassroomManageExamState.FinishExam -> {
            refreshExamList()
        }
        is ManageClassroomManageExamState.Error -> {
            Toast.makeText(context,(state.value as ManageClassroomManageExamState.Error).message,Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is ManageClassroomManageExamState.Loading -> {
            LoadingScreen()
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
                                "모든 시험" -> viewModel.getExamsInClassroom(selectedClassroom.id!!)
                                "종료된 시험" -> viewModel.getFinishedExams(selectedClassroom.id!!)
                                "취소된 시험" -> viewModel.getCancelledExams(selectedClassroom.id!!)
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
            items(exams.value.size){ idx ->
                exams.value[idx]?.let {
                    ExamItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedExam = it
                                manageExamDialogState.value = true
                            },
                        exam = it,
                        showDeleteButton = true
                    ) {
                        selectedExam = it
                        deleteExamDialogState.value = true
                    }
                }
            }
        }
        if(exams.value.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "시험이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
        if(state.value is ManageClassroomManageExamState.Error){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "정보를 불러올 수 없습니다",
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
        content = "${selectedExam?.name} 시험을 삭제하시겠습니까?",
        dialogState = deleteExamDialogState,
        cancelText = "취소",
        confirmText = "삭제",
        onCancel = { deleteExamDialogState.value = false },
        onConfirm = {
            viewModel.deleteExam(selectedExam?.id!!)
            deleteExamDialogState.value = false
        }
    )

    CreateExamDialog(
        selectedClassroomId = selectedClassroom.id!!,
        showDialog = createExamDialogState
    ){
        viewModel.createExam(it)
        createExamDialogState.value = false
    }

    if(selectedExam != null) {
        ManageExamDialog(
            selectedExam = selectedExam!!,
            showDialog = manageExamDialogState
        )
    }
}