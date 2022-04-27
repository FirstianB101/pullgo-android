package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import android.graphics.Color
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarEvent
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarViewModel
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.*
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )

    val scope = rememberCoroutineScope()

    var lessonDialogState by remember { mutableStateOf(false) }

    val createLessonDialogState = remember { mutableStateOf(false) }
    var refreshLessonsOnMonth by remember { mutableStateOf(false) }

    val user = PullgoApplication.instance?.getLoginUser()

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is CalendarViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is CalendarViewModel.UiEvent.CloseCreateLessonDialog -> {
                    createLessonDialogState.value = false
                }
            }
        }
    }

    LaunchedEffect(state.value.lessonsOnMonth){
        refreshLessonsOnMonth = true
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            CalendarBottomSheet(
                selectedDate = state.value.selectedDate?.toDayString() ?: "",
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                viewModel = viewModel,
                showDialog = { lesson ->
                    viewModel.onEvent(CalendarEvent.OnLessonSelected(lesson))
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
                        viewModel.onEvent(CalendarEvent.OnCalendarDaySelected(date))

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
                    if (refreshLessonsOnMonth) {
                        removeDecorators()
                        addDecorators(
                            CalendarSaturdayDecorator(),
                            CalendarSundayDecorator(),
                            CalendarTodayDecorator(),
                            CalendarEventDecorator(
                                R.color.statusbar_color,
                                CalendarUtils.makeCalendarDayList(state.value.lessonsOnMonth)
                            )
                        )
                        refreshLessonsOnMonth = false
                    }
                }
            }
        )

        if (lessonDialogState) {
            when {
                user?.teacher != null && state.value.selectedLesson != null-> {
                    TeacherLessonInfoDialog(
                        showDialog = lessonDialogState,
                        lesson = state.value.selectedLesson!!,
                        viewModel = viewModel,
                        onClose = {
                            lessonDialogState = false
                        }
                    )
                }
                user?.student != null && state.value.selectedLesson != null-> {
                    StudentLessonInfoDialog(
                        showDialog = lessonDialogState,
                        lesson = state.value.selectedLesson!!,
                        viewModel = viewModel,
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
                    viewModel.onEvent(CalendarEvent.GetClassroomsTeacherApplied)
                    createLessonDialogState.value = true
                },
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
    CreateLessonDialog(
        showDialog = createLessonDialogState,
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