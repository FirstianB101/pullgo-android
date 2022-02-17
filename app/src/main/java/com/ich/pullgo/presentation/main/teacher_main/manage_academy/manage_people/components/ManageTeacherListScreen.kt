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
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TeacherInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ManageTeacherItem
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyState
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageTeacherListScreen(
    selectedAcademyId: Long,
    viewModel: ManageAcademyViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    val teachers: MutableState<List<Teacher?>> = remember { mutableStateOf(listOf(null)) }
    var selectedTeacher: Teacher? by remember{ mutableStateOf(null) }

    val kickDialogState = remember{ mutableStateOf(false) }
    val teacherInfoDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getTeachersInAcademy(selectedAcademyId)
    }

    when(state.value){
        is ManageAcademyState.Loading -> {
            LoadingScreen()
        }
        is ManageAcademyState.GetTeachers -> {
            teachers.value = (state.value as ManageAcademyState.GetTeachers).teachers
            viewModel.onResultConsume()
        }
        is ManageAcademyState.KickUser -> {
            Toast.makeText(context,"선생님을 제외했습니다",Toast.LENGTH_SHORT).show()
            viewModel.getTeachersInAcademy(selectedAcademyId)
        }
    }

    LazyColumn{
        items(teachers.value.size){ idx ->
            teachers.value[idx]?.let {
                ManageTeacherItem(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedTeacher = it
                            teacherInfoDialogState.value = true
                        },
                    teacher = it,
                    showKickButton = true
                ) {
                    selectedTeacher = it
                    kickDialogState.value = true
                }
            }
        }
    }
    if(teachers.value.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "학원에 가입된 선생님이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
    if(state.value is ManageAcademyState.Error){
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
        title = "학원에서 제외",
        content = "${selectedTeacher?.account?.fullName} 선생님을 학원에서 제외하시겠습니까?",
        dialogState = kickDialogState,
        cancelText = "취소",
        confirmText = "제외",
        onCancel = { kickDialogState.value = false },
        onConfirm = {
            viewModel.kickTeacher(selectedAcademyId,selectedTeacher?.id!!)
            kickDialogState.value = false
        }
    )

    if(selectedTeacher != null) {
        TeacherInfoDialog(
            showDialog = teacherInfoDialogState,
            teacher = selectedTeacher!!,
            showRemoveIcon = false,
            onRemoveClicked = {}
        )
    }
}