package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.data.remote.dto.Schedule
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarEvent
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarViewModel
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarUtils.applyPatchedLesson
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarUtils.oneColonFormatToTwoColon
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarUtils.twoColonFormatToOneColon
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TeacherLessonInfoScreen(
    lesson: Lesson,
    onClose: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()

    var lessonName by rememberSaveable { mutableStateOf(lesson.name.toString()) }
    var lessonDate by rememberSaveable { mutableStateOf(lesson.schedule?.date.toString()) }
    var lessonBeginTime by rememberSaveable { mutableStateOf(twoColonFormatToOneColon(lesson.schedule?.beginTime.toString())) }
    var lessonEndTime by rememberSaveable { mutableStateOf(twoColonFormatToOneColon(lesson.schedule?.endTime.toString())) }

    var isEditMode by remember{ mutableStateOf(false) }

    val dateDialogState = rememberMaterialDialogState()
    val beginTimeDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()
    var deleteDialogState = remember{ mutableStateOf(false)}

    LaunchedEffect(Unit){
        viewModel.onEvent(
            CalendarEvent.GetLessonAcademyClassroomInfo(
                state.value.selectedLesson?.academyId!!,
                state.value.selectedLesson?.classroomId!!
            )
        )

        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is CalendarViewModel.UiEvent.CloseLessonDialog -> onClose()
                is CalendarViewModel.UiEvent.CopyLesson -> applyPatchedLesson(lesson, event.lesson)
            }
        }
    }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_info_24),
                contentDescription = null,
                tint = Color.DarkGray
            )
            Text(
                text = stringResource(R.string.manage_lesson),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lessonName,
            onValueChange = {lessonName = it},
            enabled = isEditMode,
            label = { Text(text = stringResource(R.string.lesson_name)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.value.classroomInfo?.name?.split(';')?.get(0) ?: "",
            onValueChange = {},
            enabled = false,
            label = { Text(text = stringResource(R.string.selected_classroom)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lessonDate,
            onValueChange = {},
            enabled = isEditMode,
            label = { Text(text = stringResource(R.string.date)) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        dateDialogState.show()
                    },
                    enabled = isEditMode
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lessonBeginTime,
            onValueChange = {},
            enabled = isEditMode,
            label = { Text(text = stringResource(R.string.begin_time)) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        beginTimeDialogState.show()
                    },
                    enabled = isEditMode
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lessonEndTime,
            onValueChange = {},
            enabled = isEditMode,
            label = { Text(text = stringResource(R.string.end_time)) },
            trailingIcon = {
                IconButton(
                    onClick = {
                        endTimeDialogState.show()
                    },
                    enabled = isEditMode
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            TextButton(
                onClick = {
                    if(isEditMode){
                        val newLesson = Lesson(
                            name = lessonName,
                            classroomId = lesson.classroomId,
                            schedule = Schedule(lessonDate, oneColonFormatToTwoColon(lessonBeginTime), oneColonFormatToTwoColon(lessonEndTime)),
                            academyId = lesson.academyId
                        )
                        viewModel.onEvent(CalendarEvent.PatchLesson(lesson.id!!,newLesson))
                    }
                    isEditMode = !isEditMode
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    tint = colorResource(R.color.secondary_color)
                )

                Text(
                    text = if(isEditMode) stringResource(R.string.modify_complete)
                            else stringResource(R.string.modify),
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
                    painter = painterResource(R.drawable.outline_delete_white_24dp),
                    contentDescription = null,
                    tint = colorResource(R.color.material_700_red)
                )

                Text(
                    text = stringResource(R.string.delete),
                    color = colorResource(R.color.material_700_red),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    TwoButtonDialog(
        title = "수업 삭제",
        content = "${lesson.name} 수업을 삭제하시겠습니까?",
        dialogState = deleteDialogState,
        cancelText = "취소",
        confirmText = "삭제",
        onCancel = { deleteDialogState.value = false },
        onConfirm = { viewModel.onEvent(CalendarEvent.DeleteLesson(lesson.id!!)) }
    )

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("OK")
            negativeButton("Cancel")
        }
    ) {
        datepicker{ date ->
            if(date.isBefore(LocalDate.now())){
                Toast.makeText(context,"과거 날짜는 선택할 수 없습니다",Toast.LENGTH_SHORT).show()
            }else{
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
        timepicker{ time ->
            if(lessonDate == LocalDate.now().toString() && time.isBefore(LocalTime.now())){
                Toast.makeText(context,"과거 시간대는 선택할 수 없습니다",Toast.LENGTH_SHORT).show()
            }else{
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
        timepicker{ time ->
            lessonEndTime = time.toString()
        }
    }
}