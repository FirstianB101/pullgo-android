package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionActivity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ManageExamAndQuestionScreen(
    selectedExam: Exam,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val finishDialogState = remember { mutableStateOf(false) }
    val cancelDialogState = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .weight(1f)
        ) {
            Text(
                text = "문제 관리하기",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "시험에 문제를 등록 / 삭제합니다",
                color = colorResource(R.color.main_color),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = colorResource(R.color.main_color)
                ),
                contentPadding = PaddingValues(14.dp),
                onClick = {
                    val intent = Intent(context,ManageQuestionActivity::class.java)
                    intent.putExtra("selectedExam",selectedExam)
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "문제 관리하기",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        Divider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.comment_cancel_exam),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.comment_cancel_exam_detail),
                color = colorResource(R.color.material_700_red),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40),
                onClick = {
                    cancelDialogState.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = colorResource(R.color.material_700_red)
                ),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel_exam),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Divider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.comment_finish_exam),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.comment_finish_exam_detail),
                color = colorResource(android.R.color.holo_orange_dark),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(40),
                onClick = {
                    finishDialogState.value = true
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = colorResource(android.R.color.holo_orange_dark)
                ),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.finish_exam),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }

    TwoButtonDialog(
        title = "시험 취소",
        content = "${selectedExam.name} 시험을 취소하시겠습니까?",
        dialogState = cancelDialogState,
        cancelText = "닫기",
        confirmText = "시험 취소",
        onCancel = { cancelDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageClassroomManageExamEvent.CancelExam)
            cancelDialogState.value = false
        }
    )

    TwoButtonDialog(
        title = "시험 종료",
        content = "${selectedExam.name} 시험을 종료하시겠습니까?",
        dialogState = finishDialogState,
        cancelText = "닫기",
        confirmText = "시험 종료",
        onCancel = { finishDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageClassroomManageExamEvent.FinishExam)
            finishDialogState.value = false
        }
    )
}