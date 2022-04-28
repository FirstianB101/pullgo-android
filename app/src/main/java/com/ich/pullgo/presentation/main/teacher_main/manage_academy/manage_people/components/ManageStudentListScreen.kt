package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
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
import com.ich.pullgo.common.components.StudentInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ManageStudentItem
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.ManagePeopleEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.ManagePeopleViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun ManageStudentListScreen(
    selectedAcademyId: Long,
    viewModel: ManagePeopleViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    val kickDialogState = remember{ mutableStateOf(false) }
    val studentInfoDialogState = remember { mutableStateOf(false) }

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
        items(state.value.studentsInAcademy.size){ idx ->
            ManageStudentItem(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        viewModel.onEvent(ManagePeopleEvent.SelectStudent(state.value.studentsInAcademy[idx]))
                        studentInfoDialogState.value = true
                    },
                student = state.value.studentsInAcademy[idx],
                showKickButton = true
            ) {
                viewModel.onEvent(ManagePeopleEvent.SelectStudent(state.value.studentsInAcademy[idx]))
                kickDialogState.value = true
            }
        }
    }
    if(state.value.studentsInAcademy.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "학원에 가입된 학생이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    TwoButtonDialog(
        title = "학원에서 제외",
        content = "${state.value.selectedStudent?.account?.fullName} 학생을 학원에서 제외하시겠습니까?",
        dialogState = kickDialogState,
        cancelText = "취소",
        confirmText = "제외",
        onCancel = { kickDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManagePeopleEvent.KickStudent(selectedAcademyId))
            kickDialogState.value = false
        }
    )

    if(state.value.selectedStudent != null) {
        StudentInfoDialog(
            showDialog = studentInfoDialogState,
            student = state.value.selectedStudent!!,
            showRemoveIcon = false,
            onRemoveClicked = {}
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}