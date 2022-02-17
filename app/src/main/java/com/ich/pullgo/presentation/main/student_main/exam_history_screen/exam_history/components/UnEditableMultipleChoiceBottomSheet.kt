package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components.CircleBackgroundText


@Composable
fun UnEditableMultipleChoiceBottomSheet(
    question: Question,
    attenderAnswer: AttenderAnswer?
){
    Column(
        Modifier
            .fillMaxWidth()
            .height(465.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "정답 확인",
                fontSize = 19.sp
            )
        }

        for(i in 1..5){
            val isCorrect = question.answer!!.contains(i)

            Row(
                modifier =
                if(isCorrect) Modifier
                    .fillMaxWidth()
                    .background(
                        colorResource(R.color.success_green),
                        RoundedCornerShape(30)
                    )
                    .padding(4.dp)
                else Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleBackgroundText(
                    text = "$i",
                    backgroundColor = if(isCorrect) colorResource(R.color.material_green)
                                        else colorResource(R.color.main_color),
                    textColor = Color.White,
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = question.choice?.get("$i") ?: "",
                    fontSize = 19.sp,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Checkbox(
                    modifier = Modifier.size(24.dp),
                    checked = attenderAnswer?.answer?.contains(i) ?: false,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(
                        checkedColor = if(isCorrect) colorResource(R.color.material_green)
                                        else colorResource(R.color.main_color),
                        uncheckedColor = if(isCorrect) colorResource(R.color.material_green)
                                        else colorResource(R.color.main_color)
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}