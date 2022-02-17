package com.ich.pullgo.presentation.main.common.components.manage_request_screen.components

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
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ClassroomItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestState
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestViewModel

@ExperimentalComposeUiApi
@Composable
fun ClassroomRequestListScreen(
    viewModel: ManageRequestViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val classrooms: MutableState<List<Classroom?>> = remember { mutableStateOf(listOf(null)) }
    var selectedClassroom: Classroom? by remember{ mutableStateOf(null) }

    val removeDialogState = remember{ mutableStateOf(false) }
    val classroomInfoDialogState = remember{ mutableStateOf(false) }

    val user = PullgoApplication.instance!!.getLoginUser()

    when(state.value){
        is ManageRequestState.ClassroomRequests -> {
            classrooms.value = (state.value as ManageRequestState.ClassroomRequests).requests
            viewModel.onResultConsume()
        }
        is ManageRequestState.RemoveClassroomRequest -> {
            Toast.makeText(context,"요청이 삭제되었습니다", Toast.LENGTH_SHORT).show()
            removeDialogState.value = false

            user?.doJob(
                ifStudent = { viewModel.getClassroomsStudentApplying(user.student?.id!!) },
                ifTeacher = { viewModel.getClassroomsTeacherApplying(user.teacher?.id!!) }
            )
            viewModel.onResultConsume()
        }
        is ManageRequestState.Loading -> {
            LoadingScreen()
        }
    }

    LazyColumn{
        items(classrooms.value.size){ idx ->
            classrooms.value[idx]?.let {
                ClassroomItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedClassroom = classrooms.value[idx]
                            classroomInfoDialogState.value = true
                        },
                    classroom = it,
                    showDeleteButton = true
                ) {
                    selectedClassroom = classrooms.value[idx]
                    removeDialogState.value = true
                }
            }
        }
    }
    if(classrooms.value.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "보낸 반 가입 요청이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
    if(state.value is ManageRequestState.Error){
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
        title = "요청 삭제",
        content = "${selectedClassroom?.name?.split(';')?.get(0)} 가입 요청을 취소하시겠습니까?",
        dialogState = removeDialogState,
        cancelText = "취소",
        confirmText = "삭제",
        onCancel = { removeDialogState.value = false },
        onConfirm = {
            user?.doJob(
                ifStudent = { viewModel.removeStudentApplyingClassroomRequest(user.student?.id!!,selectedClassroom?.id!!) },
                ifTeacher = { viewModel.removeTeacherApplyingClassroomRequest(user.teacher?.id!!,selectedClassroom?.id!!) }
            )
        }
    )

    ClassroomInfoDialog(
        showDialog = classroomInfoDialogState,
        classroom = selectedClassroom
    )
}