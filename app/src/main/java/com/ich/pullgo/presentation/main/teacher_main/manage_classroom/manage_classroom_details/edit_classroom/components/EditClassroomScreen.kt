package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom.components

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom.ManageClassroomEditClassroomEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom.ManageClassroomEditClassroomState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom.ManageClassroomEditClassroomViewModel
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.utils.WeekdayUtil
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun EditClassroomScreen(
    selectedClassroom: Classroom,
    viewModel: ManageClassroomEditClassroomViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val editDialogState = remember { mutableStateOf(false) }
    val deleteDialogState = remember { mutableStateOf(false) }

    var classroomInfo = selectedClassroom.name?.split(';')

    LaunchedEffect(Unit){
        val infos = selectedClassroom.name?.split(';')
        viewModel.onEvent(ManageClassroomEditClassroomEvent.ClassroomNameChanged(infos?.get(0) ?: ""))

        val dayList = WeekdayUtil.stringToWeekdays(infos?.get(1)!!)
        viewModel.onEvent(ManageClassroomEditClassroomEvent.ClassroomDaysChanged(dayList))
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ManageClassroomEditClassroomViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ManageClassroomEditClassroomViewModel.UiEvent.EditClassroom -> {
                    selectedClassroom.name = event.editedClassroom.name
                    classroomInfo = selectedClassroom.name?.split(';')
                    editDialogState.value = false
                    (context as Activity).setResult(RESULT_OK)
                }
                is ManageClassroomEditClassroomViewModel.UiEvent.DeleteClassroom -> {
                    (context as Activity).setResult(RESULT_OK)
                    context.finish()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(100.dp),
                text = stringResource(R.string.classroom_name),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                value = classroomInfo?.get(0) ?: "",
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                onValueChange = {}
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(100.dp),
                text = stringResource(R.string.date),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                value = classroomInfo?.get(1) ?: "",
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                onValueChange = {}
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(100.dp),
                text = stringResource(R.string.teacher_name),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                value = selectedClassroom.creator?.account?.fullName ?: "",
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                onValueChange = {}
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            TextButton(
                onClick = {
                    editDialogState.value = true
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = colorResource(R.color.secondary_color)
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = stringResource(R.string.edit),
                    color = colorResource(R.color.secondary_color),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            TextButton(
                onClick = {
                    deleteDialogState.value = true
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = colorResource(R.color.material_700_red)
                )

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = stringResource(R.string.delete),
                    color = colorResource(R.color.material_700_red),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    EditClassroomDialog(
        showDialog = editDialogState,
        selectedClassroom = selectedClassroom,
        viewModel = viewModel,
        onEditClicked = {
            viewModel.onEvent(ManageClassroomEditClassroomEvent.EditClassroom(selectedClassroom.id!!))
        }
    )

    TwoButtonDialog(
        title = stringResource(R.string.delete),
        content = "${state.value.classroomName} 반을 삭제하시겠습니까?",
        dialogState = deleteDialogState,
        cancelText = "취소",
        confirmText = "삭제",
        onCancel = { deleteDialogState.value = false },
        onConfirm = {
            viewModel.deleteClassroom(selectedClassroom.id!!)
            deleteDialogState.value = false
        }
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