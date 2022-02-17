package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.StudentInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.StudentItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.ManageClassroomManagePeopleState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.ManageClassroomManagePeopleViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageClassroomStudentList(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManagePeopleViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val students: MutableState<List<Student?>> = remember { mutableStateOf(listOf(null)) }
    var selectedStudent: Student? by remember{ mutableStateOf(null) }

    val infoDialogState = remember { mutableStateOf(false) }
    val kickDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getStudentsInClassroom(selectedClassroom.id!!)
    }

    when(state.value){
        is ManageClassroomManagePeopleState.Loading -> {
            LoadingScreen()
        }
        is ManageClassroomManagePeopleState.GetStudents -> {
            students.value = (state.value as ManageClassroomManagePeopleState.GetStudents).students
            viewModel.onResultConsume()
        }
        is ManageClassroomManagePeopleState.KickPeople -> {
            Toast.makeText(context,"학생을 반에서 제외했습니다",Toast.LENGTH_SHORT).show()
            viewModel.getStudentsInClassroom(selectedClassroom.id!!)
        }
    }

    LazyColumn{
        items(students.value.size){ idx ->
            students.value[idx]?.let {
                StudentItem(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedStudent = it
                            infoDialogState.value = true
                        },
                    student = it,
                    showDeleteButton = false,
                    showAcceptButton = false,
                    onDeleteButtonClicked = {},
                    onAcceptButtonClicked = {}
                )
            }
        }
    }
    if(students.value.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "가입된 학생이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
    if(state.value is ManageClassroomManagePeopleState.Error){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "정보를 불러올 수 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    if(selectedStudent != null) {
        StudentInfoDialog(
            showDialog = infoDialogState,
            student = selectedStudent!!,
            showRemoveIcon = true
        ){
            kickDialogState.value = true
        }
    }

    TwoButtonDialog(
        title = "학생 제외",
        content = "${selectedStudent?.account?.fullName} 학생을 반에서 제외하시겠습니까?",
        dialogState = kickDialogState,
        cancelText = "취소",
        confirmText = "제외",
        onCancel = { kickDialogState.value = false },
        onConfirm = {
            viewModel.kickStudent(selectedClassroom.id!!,selectedStudent?.id!!)
            infoDialogState.value = false
            kickDialogState.value = false
        }
    )
}