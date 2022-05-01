package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.common.components.StudentInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.StudentItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.ManageClassroomManagePeopleEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.ManageClassroomManagePeopleViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageClassroomStudentList(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManagePeopleViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val infoDialogState = remember { mutableStateOf(false) }
    val kickDialogState = remember { mutableStateOf(false) }

    LazyColumn {
        items(state.value.studentsInClassroom.size) { idx ->
            StudentItem(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        viewModel.onEvent(ManageClassroomManagePeopleEvent.SelectStudent(state.value.studentsInClassroom[idx]))
                        infoDialogState.value = true
                    },
                student = state.value.studentsInClassroom[idx],
                showDeleteButton = false,
                showAcceptButton = false,
                onDeleteButtonClicked = {},
                onAcceptButtonClicked = {}
            )
        }
    }
    if (state.value.studentsInClassroom.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "가입된 학생이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    if (state.value.selectedStudent != null) {
        StudentInfoDialog(
            showDialog = infoDialogState,
            student = state.value.selectedStudent!!,
            showRemoveIcon = true
        ) {
            kickDialogState.value = true
        }
    }

    TwoButtonDialog(
        title = "학생 제외",
        content = "${state.value.selectedStudent?.account?.fullName} 학생을 반에서 제외하시겠습니까?",
        dialogState = kickDialogState,
        cancelText = "취소",
        confirmText = "제외",
        onCancel = { kickDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageClassroomManagePeopleEvent.KickStudent(selectedClassroom.id!!))
            infoDialogState.value = false
            kickDialogState.value = false
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.value.isLoading) {
            CircularProgressIndicator()
        }
    }
}