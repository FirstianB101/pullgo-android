package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.ManageClassroomManageRequestEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.ManageClassroomManageRequestViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun ManageRequestScreen(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomManageRequestViewModel = hiltViewModel()
){
    val context = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.onEvent(ManageClassroomManageRequestEvent.GetStudentsRequests(selectedClassroom.id!!))

        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManageClassroomManageRequestViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var tabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf(stringResource(R.string.student_request), stringResource(R.string.teacher_request))

        Column {
            TabRow(selectedTabIndex = tabIndex,
                contentColor = colorResource(R.color.main_color),
                backgroundColor = Color.White) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = {
                            tabIndex = index

                            if(tabIndex == 0) viewModel.onEvent(ManageClassroomManageRequestEvent.GetStudentsRequests(selectedClassroom.id!!))
                            else viewModel.onEvent(ManageClassroomManageRequestEvent.GetTeachersRequests(selectedClassroom.id!!))
                        },
                        text = { Text(text = title, maxLines = 1, softWrap = false, color = Color.Black)}
                    )
                }
            }
            when (tabIndex) {
                0 -> {
                    ManageClassroomStudentRequestList(selectedClassroom, viewModel)
                }
                1 -> {
                    ManageClassroomTeacherRequestList(selectedClassroom, viewModel)
                }
            }
        }
    }
}