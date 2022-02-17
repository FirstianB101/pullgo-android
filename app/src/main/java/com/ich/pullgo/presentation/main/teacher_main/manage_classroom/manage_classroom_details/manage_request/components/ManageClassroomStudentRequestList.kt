package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.StudentInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.StudentItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.ManageClassroomManageRequestState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.ManageClassroomManageRequestViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageClassroomStudentRequestList(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManageRequestViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val students: MutableState<List<Student?>> = remember { mutableStateOf(listOf(null)) }
    var selectedStudent: Student? by remember{ mutableStateOf(null) }

    val infoDialogState = remember{ mutableStateOf(false) }
    val denyDialogState = remember { mutableStateOf(false ) }
    val acceptDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getStudentRequests(selectedClassroom.id!!)
    }

    when(state.value){
        is ManageClassroomManageRequestState.Loading -> {
            LoadingScreen()
        }
        is ManageClassroomManageRequestState.GetStudentRequests -> {
            students.value = (state.value as ManageClassroomManageRequestState.GetStudentRequests).students
            viewModel.onResultConsume()
        }
        is ManageClassroomManageRequestState.AcceptRequest -> {
            Toast.makeText(context,"요청을 승인했습니다", Toast.LENGTH_SHORT).show()
            viewModel.getStudentRequests(selectedClassroom.id!!)
        }
        is ManageClassroomManageRequestState.DenyRequest -> {
            Toast.makeText(context,"요청을 거절했습니다", Toast.LENGTH_SHORT).show()
            viewModel.getStudentRequests(selectedClassroom.id!!)
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
                    showDeleteButton = true,
                    showAcceptButton = true,
                    onDeleteButtonClicked = {
                        selectedStudent = it
                        denyDialogState.value = true
                    },
                    onAcceptButtonClicked = {
                        selectedStudent = it
                        acceptDialogState.value = true
                    }
                )
            }
        }
    }
    if(students.value.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "가입 요청이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
    if(state.value is ManageClassroomManageRequestState.Error){
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
            showRemoveIcon = false,
            onRemoveClicked = {}
        )
    }

    TwoButtonDialog(
        title = "요청 승인",
        content = "${selectedStudent?.account?.fullName} 학생의 요청을 승인하시겠습니까?",
        dialogState = acceptDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = "승인",
        onCancel = { acceptDialogState.value = false },
        onConfirm = {
            viewModel.acceptStudent(selectedClassroom.id!!,selectedStudent?.id!!)
            acceptDialogState.value = false
        }
    )

    TwoButtonDialog(
        title = "요청 거절",
        content = "${selectedStudent?.account?.fullName} 학생의 요청을 거절하시겠습니까?",
        dialogState = denyDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = "거절",
        onCancel = { denyDialogState.value = false },
        onConfirm = {
            viewModel.denyStudent(selectedClassroom.id!!,selectedStudent?.id!!)
            denyDialogState.value = false
        }
    )
}