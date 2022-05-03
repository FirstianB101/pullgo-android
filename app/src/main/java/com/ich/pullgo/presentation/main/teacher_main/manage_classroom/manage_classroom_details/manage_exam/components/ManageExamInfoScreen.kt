package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.copy
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.util.DurationUtil
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarUtils.oneColonFormatToTwoColon
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarUtils.twoColonFormatToOneColon
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collectLatest
import java.time.Duration

@Composable
fun ManageExamInfoScreen(
    selectedExam: Exam,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val beginDateTime = rememberSaveable { selectedExam.beginDateTime!!.split('T') }
    val endDateTime = rememberSaveable { selectedExam.endDateTime!!.split('T') }

    var examName by rememberSaveable { mutableStateOf(selectedExam.name) }
    var beginDate by rememberSaveable { mutableStateOf(beginDateTime[0]) }
    var beginTime by rememberSaveable { mutableStateOf(twoColonFormatToOneColon(beginDateTime[1])) }
    var endDate by rememberSaveable { mutableStateOf(endDateTime[0]) }
    var endTime by rememberSaveable { mutableStateOf(twoColonFormatToOneColon(endDateTime[1])) }
    var timeLimit by rememberSaveable { mutableStateOf(DurationUtil.translateDurToMinute(selectedExam.timeLimit!!)) }
    var passScore by rememberSaveable { mutableStateOf(selectedExam.passScore.toString()) }

    val beginDateDialogState = rememberMaterialDialogState()
    val beginTimeDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    var editModeOn by remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManageClassroomManageExamViewModel.UiEvent.EditExam -> {
                    selectedExam.copy(event.editedExam)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(
                Color.White, RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            enabled = editModeOn,
            modifier = Modifier.fillMaxWidth(),
            value = examName ?: "",
            onValueChange = { examName = it },
            label = { Text(text = stringResource(R.string.comment_input_exam_name)) }
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            enabled = editModeOn,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            value = beginDate ?: "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.comment_select_begin_date)) },
            trailingIcon = {
                IconButton(
                    enabled = editModeOn,
                    onClick = {
                        beginDateDialogState.show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            enabled = editModeOn,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            value = beginTime ?: "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.exam_begin_time)) },
            trailingIcon = {
                IconButton(
                    enabled = editModeOn,
                    onClick = {
                        beginTimeDialogState.show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            enabled = editModeOn,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            value = endDate ?: "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.comment_select_end_date)) },
            trailingIcon = {
                IconButton(
                    enabled = editModeOn,
                    onClick = {
                        endDateDialogState.show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            enabled = editModeOn,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            value = endTime ?: "",
            onValueChange = {},
            label = { Text(text = stringResource(R.string.comment_select_end_time)) },
            trailingIcon = {
                IconButton(
                    enabled = editModeOn,
                    onClick = {
                        endTimeDialogState.show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            enabled = editModeOn,
            modifier = Modifier.fillMaxWidth(),
            value = timeLimit,
            onValueChange = { timeLimit = it },
            label = { Text(text = stringResource(R.string.comment_input_exam_time_limit)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            enabled = editModeOn,
            modifier = Modifier.fillMaxWidth(),
            value = passScore,
            onValueChange = { passScore = it },
            label = { Text(text = stringResource(R.string.comment_input_exam_pass_score)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            TextButton(
                onClick = {
                    if(editModeOn){
                        if (!examName.isNullOrBlank() && !beginDate.isNullOrBlank() && !beginTime.isNullOrBlank() && !endDate.isNullOrBlank() && !endTime.isNullOrBlank()
                            && passScore.isNotBlank() && timeLimit.isNotBlank()
                        ) {
                            val edited = Exam(
                                name = examName,
                                beginDateTime = "${beginDate}T${oneColonFormatToTwoColon(beginTime)}",
                                endDateTime = "${endDate}T${oneColonFormatToTwoColon(endTime)}",
                                timeLimit = Duration.ofMinutes(timeLimit.toLong()).toString(),
                                passScore = passScore.toInt(),
                                creatorId = null, classroomId = null, cancelled = false, finished = false
                            )
                            viewModel.onEvent(ManageClassroomManageExamEvent.EditExam(selectedExam.id!!,edited))
                        } else {
                            Toast.makeText(context, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    editModeOn = !editModeOn
                }
            ) {
                Text(
                    text = if(editModeOn) stringResource(R.string.edit_success) else stringResource(R.string.modify),
                    color = colorResource(R.color.secondary_color),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    MaterialDialog(
        dialogState = beginDateDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date ->
            beginDate = date.toString()
        }
    }

    MaterialDialog(
        dialogState = beginTimeDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Cancel")
        }
    ) {
        timepicker { time ->
            beginTime = time.toString()
        }
    }

    MaterialDialog(
        dialogState = endDateDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date ->
            endDate = date.toString()
        }
    }

    MaterialDialog(
        dialogState = endTimeDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Cancel")
        }
    ) {
        timepicker { time ->
            endTime = time.toString()
        }
    }
}