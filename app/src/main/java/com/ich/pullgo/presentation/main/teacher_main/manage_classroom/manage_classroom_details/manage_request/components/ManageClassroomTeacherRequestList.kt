package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TeacherInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.TeacherItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.ManageClassroomManageRequestEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.ManageClassroomManageRequestViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageClassroomTeacherRequestList(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManageRequestViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()

    val infoDialogState = remember{ mutableStateOf(false) }
    val acceptDialogState = remember { mutableStateOf(false) }
    val denyDialogState = remember { mutableStateOf(false) }

    LazyColumn{
        items(state.value.teacherRequests.size){ idx ->
            TeacherItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        viewModel.onEvent(ManageClassroomManageRequestEvent.SelectTeacherRequest(state.value.teacherRequests[idx]))
                        infoDialogState.value = true
                    },
                teacher = state.value.teacherRequests[idx],
                showDeleteButton = true,
                showAcceptButton = true,
                onDeleteButtonClicked = {
                    viewModel.onEvent(ManageClassroomManageRequestEvent.SelectTeacherRequest(state.value.teacherRequests[idx]))
                    denyDialogState.value = true
                },
                onAcceptButtonClicked = {
                    viewModel.onEvent(ManageClassroomManageRequestEvent.SelectTeacherRequest(state.value.teacherRequests[idx]))
                    acceptDialogState.value = true
                }
            )
        }
    }
    if(state.value.teacherRequests.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "가입 요청이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    if(state.value.selectedTeacherRequest != null) {
        TeacherInfoDialog(
            showDialog = infoDialogState,
            teacher = state.value.selectedTeacherRequest!!,
            showRemoveIcon = false,
            onRemoveClicked = {}
        )
    }

    TwoButtonDialog(
        title = "요청 승인",
        content = "${state.value.selectedTeacherRequest?.account?.fullName} 학생의 요청을 승인하시겠습니까?",
        dialogState = acceptDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = "승인",
        onCancel = { acceptDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageClassroomManageRequestEvent.AcceptTeacher(selectedClassroom.id!!))
            acceptDialogState.value = false
        }
    )

    TwoButtonDialog(
        title = "요청 거절",
        content = "${state.value.selectedTeacherRequest?.account?.fullName} 학생의 요청을 거절하시겠습니까?",
        dialogState = denyDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = "거절",
        onCancel = { denyDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageClassroomManageRequestEvent.DenyTeacher(selectedClassroom.id!!))
            denyDialogState.value = false
        }
    )
}