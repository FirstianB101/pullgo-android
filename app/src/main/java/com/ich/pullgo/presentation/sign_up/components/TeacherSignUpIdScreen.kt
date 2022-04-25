package com.ich.pullgo.presentation.sign_up.components

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ich.pullgo.presentation.sign_up.SignUpViewModel
import com.ich.pullgo.presentation.sign_up.util.SignUpScreen

@Composable
fun TeacherSignUpIdScreen(
    navController: NavController,
    viewModel: SignUpViewModel
){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    SignUpIdScreen(
        scaffoldState = scaffoldState,
        scope = scope,
        viewModel = viewModel,
        onNextButtonClick = {
            navController.navigate(SignUpScreen.TeacherPwScreen.route)
        }
    )
}
