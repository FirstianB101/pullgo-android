package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel

@Composable
fun ManageExamStatusScreen(
    selectedExam: Exam,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    val studentAttenderMap = remember { HashMap<Long,AttenderState>() }
    val students: MutableState<List<Student?>> = remember { mutableStateOf(listOf(null)) }
    val attenders: MutableState<List<AttenderState?>> = remember { mutableStateOf(listOf(null)) }

    when(state.value){
        is ManageClassroomManageExamState.GetStudentsInClassroom -> {
            students.value = (state.value as ManageClassroomManageExamState.GetStudentsInClassroom).students
            viewModel.onResultConsume()
        }
        is ManageClassroomManageExamState.GetAttenderStates -> {
            attenders.value = (state.value as ManageClassroomManageExamState.GetAttenderStates).attenderStates

            for(attender in attenders.value)
                studentAttenderMap[attender?.attenderId!!] = attender

            viewModel.getStudentsInClassroom(selectedExam.classroomId!!)
        }
    }

    LaunchedEffect(Unit){
        viewModel.getAttenderStates(selectedExam.id!!)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn{
            items(students.value.size){ idx ->
                students.value[idx]?.let {
                    ExamStatusItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(bottom = 8.dp),
                        student = it,
                        attenderState = studentAttenderMap[it.id]
                    )
                }
            }
        }
        if(students.value.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "반에 학생이 없습니다",
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
}