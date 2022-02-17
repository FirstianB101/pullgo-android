package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.ich.pullgo.R
import com.ich.pullgo.presentation.theme.PullgoTheme
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.components.ManagePeopleScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagePeopleActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedAcademyId = intent.getLongExtra("selectedAcademyId",0L)
        setContent {
            PullgoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val scaffoldState = rememberScaffoldState()

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "구성원 관리",
                                        color = Color.White
                                    )
                                },
                                backgroundColor = colorResource(R.color.main_color)
                            )
                        }
                    ) {
                        ManagePeopleScreen(selectedAcademyId)
                    }
                }
            }
        }
    }
}