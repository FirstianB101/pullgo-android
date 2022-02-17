package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.data.remote.dto.Schedule
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarLessonState
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CreateLessonDialog(
    showDialog: MutableState<Boolean>,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.dialogState.collectAsState()

    var spinnerState by remember { mutableStateOf(false) }
    var appliedClassrooms: List<Classroom> by remember { mutableStateOf(emptyList()) }
    var selectedClassroom: Classroom? by remember { mutableStateOf(null) }

    var lessonDate by remember { mutableStateOf("") }
    var lessonBeginTime by remember { mutableStateOf("") }
    var lessonEndTime by remember { mutableStateOf("") }
    var lessonName by remember { mutableStateOf("") }

    var selectClassroomState by remember { mutableStateOf(false) }

    val dateDialogState = rememberMaterialDialogState()
    val beginTimeDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    when (state.value) {
        is CalendarLessonState.AppliedClassrooms -> {
            appliedClassrooms = (state.value as CalendarLessonState.AppliedClassrooms).classrooms
        }
        is CalendarLessonState.CreateLesson -> {
            Toast.makeText(context, "수업이 생성되었습니다", Toast.LENGTH_SHORT).show()
            showDialog.value = false
        }
    }

    if (showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            ),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(25.dp)
                        .fillMaxWidth()
                        .background(
                            Color.White, RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = stringResource(R.string.select_classroom),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        ExposedDropdownMenuBox(
                            expanded = spinnerState,
                            onExpandedChange = {
                                spinnerState = !spinnerState
                            }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedClassroom?.name?.split(';')?.get(0) ?: "",
                                onValueChange = { },
                                label = { Text(stringResource(R.string.select_classroom)) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = spinnerState
                                    )
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = spinnerState,
                                onDismissRequest = {
                                    spinnerState = false
                                }
                            ) {
                                appliedClassrooms.forEach { classroom ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedClassroom = classroom
                                            selectClassroomState = true
                                            spinnerState = false
                                        }
                                    ) {
                                        Text(classroom.name!!.split(';')[0])
                                    }
                                }
                            }
                        }
                    }

                    if (selectClassroomState) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.width(80.dp),
                                text = stringResource(R.string.select_date),
                                color = Color.Black,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                readOnly = true,
                                modifier = Modifier.width(260.dp),
                                value = lessonDate,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.comment_select_date)) },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            dateDialogState.show()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.width(80.dp),
                                text = stringResource(R.string.time),
                                color = Color.Black,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                readOnly = true,
                                modifier = Modifier.width(260.dp),
                                value = lessonBeginTime,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.comment_select_begin_time)) },
                                trailingIcon = {
                                    IconButton(
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
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.width(80.dp),
                                text = "",
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                readOnly = true,
                                modifier = Modifier.width(260.dp),
                                value = lessonEndTime,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.comment_select_end_time)) },
                                trailingIcon = {
                                    IconButton(
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
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.width(80.dp),
                                text = stringResource(R.string.lesson_name),
                                color = Color.Black,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                modifier = Modifier.width(260.dp),
                                value = lessonName,
                                onValueChange = { lessonName = it },
                                label = { Text(text = stringResource(R.string.comment_input_lesson_name)) }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            TextButton(
                                onClick = {
                                    val newLesson = Lesson(
                                        name = lessonName,
                                        classroomId = selectedClassroom?.id!!,
                                        schedule = Schedule(
                                            lessonDate,
                                            oneColonFormatToTwoColon(lessonBeginTime),
                                            oneColonFormatToTwoColon(lessonEndTime)
                                        ),
                                        academyId = null
                                    )

                                    if (checkLessonInfoFilled(
                                            selectedClassroom,
                                            lessonDate,
                                            lessonBeginTime,
                                            lessonEndTime,
                                            lessonName
                                        )
                                    ) {
                                        viewModel.createLesson(newLesson)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "입력되지 않은 정보가 있습니다",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.create),
                                    color = colorResource(R.color.secondary_color),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(5.dp))

                            TextButton(
                                onClick = {
                                    showDialog.value = false
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.cancel),
                                    color = colorResource(R.color.secondary_color),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton("OK")
                negativeButton("Cancel")
            }
        ) {
            datepicker { date ->
                if (date.isBefore(LocalDate.now())) {
                    Toast.makeText(context, "과거 날짜는 선택할 수 없습니다", Toast.LENGTH_SHORT).show()
                } else {
                    lessonDate = date.toString()
                }
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
                if (lessonDate == LocalDate.now().toString() && time.isBefore(LocalTime.now())) {
                    Toast.makeText(context, "과거 시간대는 선택할 수 없습니다", Toast.LENGTH_SHORT).show()
                } else {
                    lessonBeginTime = time.toString()
                }
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
                lessonEndTime = time.toString()
            }
        }
    }
}

private fun checkLessonInfoFilled(
    classroom: Classroom?,
    date: String,
    beginTime: String,
    endTime: String,
    name: String
): Boolean {
    return classroom != null && date.isNotBlank() && beginTime.isNotBlank() && endTime.isNotBlank() && name.isNotBlank()
}