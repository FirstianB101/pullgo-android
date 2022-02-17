package com.ich.pullgo.presentation.sign_up.components

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import com.ich.pullgo.presentation.sign_up.util.SignUpScreen

@Composable
fun StudentSignUpPwScreen(
    navController: NavController,
    username: String
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val password = rememberSaveable { mutableStateOf("") }
    val passwordCheck = rememberSaveable { mutableStateOf("") }

    SignUpPwScreen(
        scaffoldState = scaffoldState,
        scope = scope,
        pwState = password,
        pwCheckState = passwordCheck
    ) {
        navController.navigate(
            SignUpScreen.StudentInfoScreen.route + "?username=${username}&password=${password.value}"
        )
    }
}