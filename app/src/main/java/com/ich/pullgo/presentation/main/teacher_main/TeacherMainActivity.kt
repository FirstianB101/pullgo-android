package com.ich.pullgo.presentation.main.teacher_main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import com.ich.pullgo.presentation.theme.PullgoTheme
import com.ich.pullgo.presentation.main.teacher_main.components.TeacherMainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherMainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PullgoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val academyExist = intent.getBooleanExtra("appliedAcademyExist",false)
                    TeacherMainScreen(academyExist = academyExist)
                }
            }
        }
    }
}
