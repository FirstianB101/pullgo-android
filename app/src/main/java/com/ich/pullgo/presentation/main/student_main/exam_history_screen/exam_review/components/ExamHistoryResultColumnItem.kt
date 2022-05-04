package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExamHistoryResultColumnItem(
    modifier: Modifier = Modifier,
    questionNum: Int,
    isCorrect: Boolean
){
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = "$questionNum",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        if(isCorrect){
            Box(modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
                .weight(2f)
                .align(Alignment.CenterVertically)
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.RadioButtonUnchecked,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }else{
            Box(modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
                .weight(2f)
                .align(Alignment.CenterVertically)
            ){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }
    }
}