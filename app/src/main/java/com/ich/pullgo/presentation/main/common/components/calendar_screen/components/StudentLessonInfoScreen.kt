package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarLessonState
import com.ich.pullgo.presentation.main.common.components.calendar_screen.CalendarViewModel

private val titles = listOf(
    R.string.academy_name,
    R.string.classroom_name,
    R.string.lesson_name,
    R.string.lesson_date,
    R.string.lesson_time
)

private val infos = arrayOf(
    "",
    "",
    "",
    "",
    ""
)

@Composable
fun StudentLessonInfoScreen(
    lesson: Lesson,
    viewModel: CalendarViewModel = hiltViewModel(),
    onButtonClick: () -> Unit
) {
    val state = viewModel.dialogState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAcademySuchLesson(lesson.academyId!!)
    }

    when (state.value) {
        is CalendarLessonState.GetAcademy -> {
            infos[0] = (state.value as CalendarLessonState.GetAcademy).academy.name.toString()
            viewModel.getClassroomSuchLesson(lesson.classroomId!!)
        }
        is CalendarLessonState.GetClassroom -> {
            infos[1] =
                (state.value as CalendarLessonState.GetClassroom).classroom.name?.split(';')?.get(0)
                    .toString()
        }
        is CalendarLessonState.Loading -> {
            LoadingScreen()
        }
        is CalendarLessonState.Error -> {
            Toast.makeText(context, (state.value as CalendarLessonState.Error).message,Toast.LENGTH_SHORT).show()
        }
    }

    infos[2] = lesson.name.toString()
    infos[3] = lesson.schedule?.date.toString()
    infos[4] = "${changeDateFormat(lesson.schedule?.beginTime!!)} ~ ${changeDateFormat(lesson.schedule?.endTime!!)}"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp)
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
                text = stringResource(R.string.lesson_info),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
        
        Spacer(modifier = Modifier.height(30.dp))

        for (i in 0..4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.width(100.dp),
                    text = stringResource(titles[i]),
                    color = Color.Black,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    modifier = Modifier.width(150.dp),
                    text = infos[i],
                    color = Color.Gray,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onButtonClick
        ) {
            Text(
                text = stringResource(R.string.confirm),
                color = colorResource(R.color.main_color),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

fun changeDateFormat(date: String): String {
    val time = date.split(':')
    return "${time[0]}:${time[1]}"
}