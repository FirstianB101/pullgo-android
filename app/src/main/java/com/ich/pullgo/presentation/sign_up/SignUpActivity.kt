package com.ich.pullgo.presentation.sign_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ich.pullgo.presentation.sign_up.components.*
import com.ich.pullgo.presentation.sign_up.components.StudentSignUpPwScreen
import com.ich.pullgo.presentation.sign_up.util.SignUpScreen
import com.ich.pullgo.presentation.theme.PullgoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val viewModel: SignUpViewModel = hiltViewModel()

            PullgoTheme {
                NavHost(
                    navController = navController,
                    startDestination = SignUpScreen.SignUpMainScreen.route
                ){
                    composable(route = SignUpScreen.SignUpMainScreen.route){
                        SignUpMainScreen(navController = navController)
                    }
                    composable(route = SignUpScreen.StudentIdScreen.route){
                        StudentSignUpIdScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(route = SignUpScreen.StudentPwScreen.route){
                        StudentSignUpPwScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(route = SignUpScreen.StudentInfoScreen.route){
                        StudentSignUpInfoScreen(viewModel = viewModel)
                    }
                    composable(route = SignUpScreen.TeacherIdScreen.route){
                        TeacherSignUpIdScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(route = SignUpScreen.TeacherPwScreen.route){
                        TeacherSignUpPwScreen(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    composable(route = SignUpScreen.TeacherInfoScreen.route){
                        TeacherSignUpInfoScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}