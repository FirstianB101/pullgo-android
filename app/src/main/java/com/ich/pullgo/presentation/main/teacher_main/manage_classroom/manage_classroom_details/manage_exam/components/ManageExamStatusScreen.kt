package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel

@Composable
fun ManageExamStatusScreen(
    selectedExam: Exam,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.onEvent(ManageClassroomManageExamEvent.GetStudentStateMap(selectedExam.id!!))
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn{
            items(state.value.studentsInClassroom.size){ idx ->
                ExamStatusItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(bottom = 8.dp),
                    student = state.value.studentsInClassroom[idx],
                    attenderState = state.value.studentStateMap[state.value.studentsInClassroom[idx].id!!]
                )
            }
        }
        if(state.value.studentsInClassroom.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "반에 학생이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}