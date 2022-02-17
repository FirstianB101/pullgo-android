package com.ich.pullgo.common.components.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.util.DurationUtil

@Composable
fun ExamItem(
    modifier: Modifier = Modifier,
    exam: Exam,
    showDeleteButton: Boolean,
    onDeleteButtonClicked: () -> Unit
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${exam.name}",
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                if(showDeleteButton){
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = onDeleteButtonClicked
                    ) {
                        Icon(Icons.Filled.Clear, "")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = getDurationString(exam.beginDateTime.toString(),exam.endDateTime.toString()),
                color = Color.Gray,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "제한 시간: ${DurationUtil.translateDurToMinute(exam.timeLimit!!)}분 / 기준: ${exam.passScore}점",
                color = colorResource(R.color.secondary_color),
                fontSize = 17.sp
            )
        }
    }
}

fun getDurationString(beginTime: String, endTime: String): String{
    val begin = DurationUtil.translateISO8601Format(beginTime)
    val end = DurationUtil.translateISO8601Format(endTime)
    return "$begin ~ $end"
}