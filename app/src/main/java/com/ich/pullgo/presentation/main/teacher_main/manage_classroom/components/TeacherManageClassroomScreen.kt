package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.components

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.items.ClassroomItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomViewModel
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.ManageClassroomDetailsActivity
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TeacherManageClassroomScreen(
    viewModel: ManageClassroomViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val createDialogState = remember{ mutableStateOf(false)}

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK){
                viewModel.onEvent(ManageClassroomEvent.ResetClassroomList)
            }
        }
    )

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManageClassroomViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ManageClassroomViewModel.UiEvent.CloseCreateDialog -> {
                    createDialogState.value = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(state.value.appliedClassrooms.size) { idx ->
                ClassroomItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            val selectedClassroom = state.value.appliedClassrooms[idx]
                            viewModel.onEvent(ManageClassroomEvent.SelectClassroom(selectedClassroom))

                            val intent = Intent(context,ManageClassroomDetailsActivity::class.java)
                            intent.putExtra("selectedClassroom",selectedClassroom)

                            launcher.launch(intent)
                        },
                    classroom = state.value.appliedClassrooms[idx],
                    showDeleteButton = false,
                    onDeleteButtonClicked = {}
                )
            }
        }
        if (state.value.appliedClassrooms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "가입된 반이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                createDialogState.value = true
                viewModel.onEvent(ManageClassroomEvent.GetAppliedAcademies)
            },
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    CreateClassroomDialog(
        showDialog = createDialogState,
        viewModel = viewModel
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}