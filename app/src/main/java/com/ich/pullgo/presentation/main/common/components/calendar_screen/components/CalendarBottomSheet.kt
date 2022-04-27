package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarLessonState
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CalendarBottomSheet(
    selectedDate: String,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    showDialog: (Lesson) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    BackHandler(enabled = bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(350.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            val lessons = state.value.lessonsOnDate

            Text(
                text = if (lessons.isEmpty()) "해당 날짜에 수업이 없습니다"
                else "${lessons.size}개의 수업이 있습니다",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(com.ich.pullgo.R.color.secondary_color)
            )
        }

        val lessons = state.value.lessonsOnDate
        LazyColumn {
            items(lessons.size) { idx ->
                CalendarBottomSheetItem(lesson = lessons[idx]) {
                    showDialog(lessons[idx])
                }
            }
        }
    }
}