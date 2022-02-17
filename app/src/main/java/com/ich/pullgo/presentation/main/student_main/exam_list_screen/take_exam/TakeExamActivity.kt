package com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam.components.TakeExamMainScreen
import com.ich.pullgo.ui.theme.PullgoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TakeExamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedExam = intent.getSerializableExtra("selectedExam") as Exam
        val selectedState = intent.getSerializableExtra("selectedState") as AttenderState

        setContent {
            PullgoTheme {
                TakeExamMainScreen(
                    selectedExam = selectedExam,
                    selectedState = selectedState
                )
            }
        }
    }
}