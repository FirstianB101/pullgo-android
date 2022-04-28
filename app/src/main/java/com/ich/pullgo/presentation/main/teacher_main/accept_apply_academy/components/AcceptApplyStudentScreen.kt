package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.StudentItem
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyEvent
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyViewModel

@Composable
fun AcceptApplyStudentScreen(
    viewModel: AcceptApplyAcademyViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()

    val removeDialogState = remember{ mutableStateOf(false) }
    val acceptDialogState = remember{ mutableStateOf(false) }

    LazyColumn{
        items(state.value.studentRequests.size){ idx ->
            StudentItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                student = state.value.studentRequests[idx],
                showDeleteButton = true,
                showAcceptButton = true,
                onDeleteButtonClicked = {
                    viewModel.onEvent(AcceptApplyAcademyEvent.SelectStudent(state.value.studentRequests[idx]))
                    removeDialogState.value = true
                },
                onAcceptButtonClicked = {
                    viewModel.onEvent(AcceptApplyAcademyEvent.SelectStudent(state.value.studentRequests[idx]))
                    acceptDialogState.value = true
                }
            )
        }
    }
    if(state.value.studentRequests.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "가입 요청이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    TwoButtonDialog(
        title = "요청 거절",
        content = "${state.value.selectedStudent?.account?.fullName} 학생의 가입 요청을 거절하시겠습니까?",
        dialogState = removeDialogState,
        cancelText = "취소",
        confirmText = "거절",
        onCancel = { removeDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(AcceptApplyAcademyEvent.DenyStudentRequest)
            removeDialogState.value = false
        }
    )

    TwoButtonDialog(
        title = "요청 수락",
        content = "${state.value.selectedStudent?.account?.fullName} 학생의 가입 요청을 수락하시겠습니까?",
        dialogState = acceptDialogState,
        cancelText = "취소",
        confirmText = "수락",
        onCancel = { acceptDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(AcceptApplyAcademyEvent.AcceptStudentRequest)
            acceptDialogState.value = false
        }
    )
}