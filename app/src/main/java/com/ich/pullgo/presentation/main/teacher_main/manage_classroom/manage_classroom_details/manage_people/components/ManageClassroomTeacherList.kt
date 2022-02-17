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
import com.ich.pullgo.common.components.TeacherInfoDialog
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.TeacherItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.ManageClassroomManagePeopleState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.ManageClassroomManagePeopleViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageClassroomTeacherList(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManagePeopleViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val teachers: MutableState<List<Teacher?>> = remember { mutableStateOf(listOf(null)) }
    var selectedTeacher: Teacher? by remember{ mutableStateOf(null) }

    val infoDialogState = remember { mutableStateOf(false) }
    val kickDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getTeachersInClassroom(selectedClassroom.id!!)
    }

    when(state.value){
        is ManageClassroomManagePeopleState.Loading -> {
            LoadingScreen()
        }
        is ManageClassroomManagePeopleState.GetTeachers -> {
            teachers.value = (state.value as ManageClassroomManagePeopleState.GetTeachers).teachers
            viewModel.onResultConsume()
        }
        is ManageClassroomManagePeopleState.KickPeople -> {
            Toast.makeText(context,"선생님을 반에서 제외했습니다", Toast.LENGTH_SHORT).show()
            viewModel.getTeachersInClassroom(selectedClassroom.id!!)
        }
    }

    LazyColumn{
        items(teachers.value.size){ idx ->
            teachers.value[idx]?.let {
                TeacherItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedTeacher = it
                            infoDialogState.value = true
                        },
                    teacher = it,
                    showDeleteButton = false,
                    showAcceptButton = false,
                    onDeleteButtonClicked = {},
                    onAcceptButtonClicked = {}
                )
            }
        }
    }
    if(teachers.value.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "가입된 선생님이 없습니다",
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

    if(selectedTeacher != null) {
        TeacherInfoDialog(
            showDialog = infoDialogState,
            teacher = selectedTeacher!!,
            showRemoveIcon = true
        ){
            kickDialogState.value = true
        }
    }

    TwoButtonDialog(
        title = "선생님 제외",
        content = "${selectedTeacher?.account?.fullName} 선생님을 반에서 제외하시겠습니까?",
        dialogState = kickDialogState,
        cancelText = "취소",
        confirmText = "제외",
        onCancel = { kickDialogState.value = false },
        onConfirm = {
            viewModel.kickTeacher(selectedClassroom.id!!,selectedTeacher?.id!!)
            infoDialogState.value = false
            kickDialogState.value = false
        }
    )
}