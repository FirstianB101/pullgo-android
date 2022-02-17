package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.StudentItem
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyState
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyViewModel

@Composable
fun AcceptApplyStudentScreen(
    selectedAcademy: Academy,
    viewModel: AcceptApplyAcademyViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    val students: MutableState<List<Student?>> = remember { mutableStateOf(listOf(null)) }
    var selectedStudent: Student? by remember{ mutableStateOf(null) }

    val removeDialogState = remember{ mutableStateOf(false) }
    val acceptDialogState = remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getStudentRequests(selectedAcademy.id!!)
    }

    when(state.value){
        is AcceptApplyAcademyState.GetStudentRequests -> {
            students.value = (state.value as AcceptApplyAcademyState.GetStudentRequests).students
            viewModel.onResultConsume()
        }
        is AcceptApplyAcademyState.AcceptRequest -> {
            Toast.makeText(context,"가입 요청을 수락했습니다", Toast.LENGTH_SHORT).show()
            viewModel.getStudentRequests(selectedAcademy.id!!)
            viewModel.onResultConsume()
        }
        is AcceptApplyAcademyState.DenyRequest -> {
            Toast.makeText(context,"가입 요청을 거절했습니다", Toast.LENGTH_SHORT).show()
            viewModel.getStudentRequests(selectedAcademy.id!!)
            viewModel.onResultConsume()
        }
    }

    LazyColumn{
        items(students.value.size){ idx ->
            students.value[idx]?.let {
                StudentItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    student = it,
                    showDeleteButton = true,
                    showAcceptButton = true,
                    onDeleteButtonClicked = {
                        selectedStudent = it
                        removeDialogState.value = true
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
    if(state.value is AcceptApplyAcademyState.Error){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "정보를 불러올 수 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    TwoButtonDialog(
        title = "요청 거절",
        content = "${selectedStudent?.account?.fullName} 학생의 가입 요청을 거절하시겠습니까?",
        dialogState = removeDialogState,
        cancelText = "취소",
        confirmText = "거절",
        onCancel = { removeDialogState.value = false },
        onConfirm = {
            viewModel.denyStudentRequest(selectedStudent?.id!!,selectedAcademy.id!!)
            removeDialogState.value = false
        }
    )

    TwoButtonDialog(
        title = "요청 수락",
        content = "${selectedStudent?.account?.fullName} 학생의 가입 요청을 수락하시겠습니까?",
        dialogState = acceptDialogState,
        cancelText = "취소",
        confirmText = "수락",
        onCancel = { acceptDialogState.value = false },
        onConfirm = {
            viewModel.acceptStudentRequest(selectedStudent?.id!!,selectedAcademy.id!!)
            acceptDialogState.value = false
        }
    )
}