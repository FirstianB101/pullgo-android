package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExamHistoryResultDialog(
    showDialog: MutableState<Boolean>,
    questions: List<Question>,
    answerMap: Map<Long,AttenderAnswer>,
    scoreText: String
){
    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            ),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(25.dp)
                        .fillMaxWidth()
                        .background(
                            Color.White, RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    item {
                        Text(
                            text = scoreText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(questions.size){ idx ->
                        ExamHistoryResultColumnItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp),
                            questionNum = idx + 1,
                            isCorrect = isAnswerCorrect(questions[idx],answerMap[questions[idx].id])
                        )
                    }
                }
            }
        }
    }
}