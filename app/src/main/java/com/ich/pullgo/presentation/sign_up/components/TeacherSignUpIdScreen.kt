package com.ich.pullgo.presentation.sign_up.components

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import com.ich.pullgo.presentation.sign_up.SignUpScreen

@Composable
fun TeacherSignUpIdScreen(
    navController: NavController
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idState = rememberSaveable { mutableStateOf("") }

    SignUpIdScreen(
        scaffoldState = scaffoldState,
        scope = scope,
        idState = idState,
        onNextButtonClick = {
            navController.navigate(SignUpScreen.TeacherPwScreen.route + "/${idState.value}")
        }
    )
}
