package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components.ManageQuestionScreen
import com.ich.pullgo.ui.theme.PullgoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageQuestionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedExam = intent.getSerializableExtra("selectedExam") as Exam
        setContent {
            PullgoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ManageQuestionScreen(selectedExam)
                }
            }
        }
    }
}