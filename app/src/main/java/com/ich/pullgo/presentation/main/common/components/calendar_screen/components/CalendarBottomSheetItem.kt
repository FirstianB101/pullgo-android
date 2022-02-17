package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Lesson

@Composable
fun CalendarBottomSheetItem(
    lesson: Lesson,
    onLessonClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable{ onLessonClick() },
        elevation = 5.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ){
            Text(
                text = getLessonTimeFromLesson(lesson),
                fontSize = 16.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = lesson.name.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

fun getLessonTimeFromLesson(lesson: Lesson): String{
    val startHour = lesson.schedule?.beginTime?.split(':')?.get(0)
    val startMin = lesson.schedule?.beginTime?.split(':')?.get(1)
    val endHour = lesson.schedule?.endTime?.split(':')?.get(0)
    val endMin = lesson.schedule?.endTime?.split(':')?.get(1)

    return "$startHour:$startMin ~ $endHour:$endMin"
}