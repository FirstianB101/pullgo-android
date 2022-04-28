package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ich.pullgo.common.components.TeacherInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ManageTeacherItem
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.ManagePeopleEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.ManagePeopleViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun ManageTeacherListScreen(
    selectedAcademyId: Long,
    viewModel: ManagePeopleViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    val kickDialogState = remember{ mutableStateOf(false) }
    val teacherInfoDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManagePeopleViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LazyColumn{
        items(state.value.teachersInAcademy.size){ idx ->
            ManageTeacherItem(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        viewModel.onEvent(ManagePeopleEvent.SelectTeacher(state.value.teachersInAcademy[idx]))
                        teacherInfoDialogState.value = true
                    },
                teacher = state.value.teachersInAcademy[idx],
                showKickButton = true
            ) {
                viewModel.onEvent(ManagePeopleEvent.SelectTeacher(state.value.teachersInAcademy[idx]))
                kickDialogState.value = true
            }
        }
    }
    if(state.value.teachersInAcademy.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "학원에 가입된 선생님이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    TwoButtonDialog(
        title = "학원에서 제외",
        content = "${state.value.selectedTeacher?.account?.fullName} 선생님을 학원에서 제외하시겠습니까?",
        dialogState = kickDialogState,
        cancelText = "취소",
        confirmText = "제외",
        onCancel = { kickDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManagePeopleEvent.KickTeacher(selectedAcademyId))
            kickDialogState.value = false
        }
    )

    if(state.value.selectedTeacher != null) {
        TeacherInfoDialog(
            showDialog = teacherInfoDialogState,
            teacher = state.value.selectedTeacher!!,
            showRemoveIcon = false,
            onRemoveClicked = {}
        )
    }
}