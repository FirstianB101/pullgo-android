package com.ich.pullgo.presentation.student_main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.presentation.student_main.ui.theme.PullgoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PullgoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    if(intent.getBooleanExtra("appliedAcademyExist",false)){
                        Text(text = "Student Main Activity (Academy Exist)")
                    }else{
                        Text(text = "Student Main Activity (No Academy)")
                    }
                    BackHandler{
                        PullgoApplication.instance?.logoutUser()
                        finish()
                    }
                }
            }
        }
    }
}
