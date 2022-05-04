package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.theme.PullgoTheme
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review.components.ExamHistoryMainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamReviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedExam = intent.getSerializableExtra("selectedExam") as Exam
        val attenderState = intent.getSerializableExtra("attenderState") as AttenderState

        setContent {
            PullgoTheme {
                ExamHistoryMainScreen(
                    selectedExam = selectedExam,
                    attenderState = attenderState
                )
            }
        }
    }
}