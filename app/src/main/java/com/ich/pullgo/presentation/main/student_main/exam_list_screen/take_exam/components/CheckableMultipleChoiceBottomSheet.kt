package com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam.components

import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components.CircleBackgroundText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
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
import com.ich.pullgo.domain.model.Question

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckableMultipleChoiceBottomSheet(
    question: Question,
    onAnswerChanged: (List<Boolean>) -> Unit
){
    val checkboxStates: List<MutableState<Boolean>> = remember{
        List(5){ idx ->
            mutableStateOf(false)
        }
    }

    LaunchedEffect(question){
        checkboxStates.forEach{ state ->
            state.value = false
        }
    }

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
                text = "답안 선택",
                fontSize = 19.sp
            )
        }

        for(i in 0..4){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleBackgroundText(
                    text = "${i+1}",
                    backgroundColor = colorResource(R.color.main_color),
                    textColor = Color.White,
                    fontSize = 19.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = question.choice?.get("${i+1}") ?: "",
                    fontSize = 19.sp,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Checkbox(
                    modifier = Modifier.size(24.dp),
                    checked = checkboxStates[i].value,
                    onCheckedChange = {
                        checkboxStates[i].value = it
                        if(checkboxStates[i].value){
                            onAnswerChanged(checkboxStates.map{v->v.value})
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(R.color.main_color),
                        uncheckedColor = colorResource(R.color.main_color)
                    )
                )
            }
        }
    }
}