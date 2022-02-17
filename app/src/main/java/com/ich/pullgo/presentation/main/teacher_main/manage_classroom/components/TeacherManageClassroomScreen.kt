package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.components

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.items.ClassroomItem
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomViewModel
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.ManageClassroomDetailsActivity

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TeacherManageClassroomScreen(
    viewModel: ManageClassroomViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    var appliedClassrooms: List<Classroom?> by remember { mutableStateOf(listOf(null)) }
    var selectedClassroom: Classroom? by remember { mutableStateOf(null) }

    val createDialogState = remember{ mutableStateOf(false)}

    val user = PullgoApplication.instance?.getLoginUser()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK){
                viewModel.getClassroomsTeacherApplied(user?.teacher?.id!!)
            }
        }
    )

    LaunchedEffect(Unit){
        viewModel.getClassroomsTeacherApplied(user?.teacher?.id!!)
    }

    when (state.value) {
        is ManageClassroomState.Loading -> {
            LoadingScreen()
        }
        is ManageClassroomState.CreateClassroom -> {
            Toast.makeText(context,"반을 생성하였습니다",Toast.LENGTH_SHORT).show()
            viewModel.getClassroomsTeacherApplied(user?.teacher?.id!!)
        }
        is ManageClassroomState.GetClassrooms -> {
            appliedClassrooms = (state.value as ManageClassroomState.GetClassrooms).classrooms
            viewModel.onResultConsume()
        }
        is ManageClassroomState.Error -> {
            Toast.makeText(context, (state.value as ManageClassroomState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(appliedClassrooms.size) { idx ->
                appliedClassrooms[idx]?.let {
                    ClassroomItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedClassroom = it

                                val intent = Intent(context,ManageClassroomDetailsActivity::class.java)
                                intent.putExtra("selectedClassroom",selectedClassroom)

                                launcher.launch(intent)
                            },
                        classroom = it,
                        showDeleteButton = false,
                        onDeleteButtonClicked = {}
                    )
                }
            }
        }
        if (appliedClassrooms.isEmpty()) {
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
                viewModel.getAppliedAcademies(user?.teacher?.id!!)
            },
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    CreateClassroomDialog(showDialog = createDialogState)
}