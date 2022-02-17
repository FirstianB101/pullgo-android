package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditableMultipleChoiceBottomSheet(
    choice: Map<String,String>?,
    answer: List<Int>,
    onChoiceChanged: (String,String) -> Unit,
    onAnswerChanged: (List<Boolean>) -> Unit
){
    val choiceTexts: List<MutableState<String>> = remember{
        List(5){ idx ->
            mutableStateOf(choice?.get("${idx+1}") ?: "")
        }
    }

    val checkboxStates: List<MutableState<Boolean>> = remember{
        List(5){ idx ->
            mutableStateOf(answer.contains(idx+1))
        }
    }

    LaunchedEffect(key1 = answer, key2 = choice){
        for(i in choiceTexts.indices)
            choiceTexts[i].value = choice?.get("${i+1}") ?: ""

        for(i in checkboxStates.indices)
            checkboxStates[i].value = answer.contains(i+1)
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
                text = "보기 관리",
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

                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = choiceTexts[i].value,
                    onValueChange = {
                        choiceTexts[i].value = it
                        onChoiceChanged("${i+1}",choiceTexts[i].value)
                    },
                    textStyle = TextStyle(fontSize = 19.sp),
                    decorationBox = { innerTextField ->
                        Row{
                            if (choiceTexts[i].value.isBlank()) {
                                Text(
                                    text = "보기 입력",
                                    fontSize = 19.sp,
                                    color = Color.LightGray
                                )
                            }
                            innerTextField()
                        }
                    },
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