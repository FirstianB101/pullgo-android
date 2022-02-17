package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarLessonState
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarViewModel
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarEventDecorator
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarSaturdayDecorator
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarSundayDecorator
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.CalendarTodayDecorator
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.calendarState.collectAsState()
    val dialogState = viewModel.dialogState.collectAsState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )

    val scope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(CalendarDay.today()) }
    var lessonDialogState by remember { mutableStateOf(false) }
    var selectedLesson by remember { mutableStateOf(Lesson(null, null, null, null)) }
    var eventDays by remember{ mutableStateOf<List<Lesson>>(emptyList())}

    val createLessonDialogState = remember { mutableStateOf(false) }

    val user = PullgoApplication.instance?.getLoginUser()

    LaunchedEffect(Unit) {
        user?.doJob(
            ifStudent = { viewModel.getStudentLessonsOnMonth(user.student?.id!!) },
            ifTeacher = { viewModel.getTeacherLessonsOnMonth(user.teacher?.id!!) }
        )
    }

    when(state.value){
        is CalendarLessonState.LessonsOnMonth -> {
            eventDays = (state.value as CalendarLessonState.LessonsOnMonth).lessons
            viewModel.onDialogResultConsume()
        }
    }

    when(dialogState.value){
        is CalendarLessonState.PatchLesson, is CalendarLessonState.DeleteLesson, is CalendarLessonState.CreateLesson -> {
            user?.doJob(
                ifStudent = { viewModel.getStudentLessonsOnMonth(user.student?.id!!) },
                ifTeacher = { viewModel.getTeacherLessonsOnMonth(user.teacher?.id!!) }
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            CalendarBottomSheet(
                selectedDate = calendarDayToString(selectedDate),
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                showDialog = { lesson ->
                    selectedLesson = lesson
                    lessonDialogState = true
                }
            )
        }, sheetPeekHeight = 0.dp
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MaterialCalendarView(context).apply {
                    selectionColor = Color.parseColor("#FFEBAE")
                    addDecorators(
                        CalendarSaturdayDecorator(),
                        CalendarSundayDecorator(),
                        CalendarTodayDecorator(),
                    )
                    setOnDateChangedListener { widget, date, selected ->
                        selectedDate = date
                        this.selectedDate = selectedDate

                        user?.doJob(
                            ifStudent = { viewModel.getStudentLessonsOnDate(
                                user.student?.id!!,
                                calendarDayToString(selectedDate)
                            )},
                            ifTeacher = { viewModel.getTeacherLessonsOnDate(
                                user.teacher?.id!!,
                                calendarDayToString(selectedDate)
                            )}
                        )

                        scope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    }
                }
            },
            update = { view ->
                view.apply {
                    if (state.value is CalendarLessonState.LessonsOnMonth) {
                        removeDecorators()
                        addDecorators(
                            CalendarSaturdayDecorator(),
                            CalendarSundayDecorator(),
                            CalendarTodayDecorator(),
                            CalendarEventDecorator(
                                R.color.statusbar_color,
                                makeCalendarDayList(eventDays)
                            )
                        )
                        viewModel.onCalendarResultConsume()
                    }
                }
            }
        )

        if (lessonDialogState) {
            when {
                user?.teacher != null -> {
                    TeacherLessonInfoDialog(
                        showDialog = lessonDialogState,
                        lesson = selectedLesson,
                        onClose = {
                            lessonDialogState = false
                        }
                    )
                }
                user?.student != null -> {
                    StudentLessonInfoDialog(
                        showDialog = lessonDialogState,
                        lesson = selectedLesson,
                        onClose = {
                            lessonDialogState = false
                        }
                    )
                }
            }
        }
    }

    if (user?.teacher != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    createLessonDialogState.value = true
                    viewModel.getClassroomsTeacherApplied(user.teacher?.id!!)
                },
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
    CreateLessonDialog(
        showDialog = createLessonDialogState
    )
}

private fun makeCalendarDayList(lessons: List<Lesson>): List<CalendarDay> {
    val dates = ArrayList<String>()
    val calList = ArrayList<CalendarDay>()

    for (lesson in lessons) {
        dates.add(lesson.schedule?.date!!)
    }

    var tmp: List<Int>
    for (date in dates) {
        //연도-월-일 형식이며 월은 0부터 시작하므로 1 빼줌
        tmp = date.split('-').map { d -> d.toInt() }
        calList.add(CalendarDay.from(tmp[0], tmp[1] - 1, tmp[2]))
    }

    return calList
}

fun calendarDayToString(date: CalendarDay): String {
    return String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)
}