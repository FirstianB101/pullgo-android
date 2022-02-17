package com.ich.pullgo.presentation.main.student_main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.presentation.main.student_main.components.StudentMainScreen
import com.ich.pullgo.ui.theme.PullgoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentMainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PullgoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val academyExist = intent.getBooleanExtra("appliedAcademyExist",false)
                    StudentMainScreen(academyExist = academyExist)
                }
            }
        }
    }
}
