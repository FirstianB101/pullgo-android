package com.ich.pullgo.presentation.sign_up

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ich.pullgo.presentation.sign_up.components.*
import com.ich.pullgo.presentation.sign_up.components.StudentSignUpPwScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SignUpScreen.SignUpMainScreen.route
            ){
                composable(route = SignUpScreen.SignUpMainScreen.route){
                    SignUpMainScreen(navController = navController)
                }
                composable(route = SignUpScreen.StudentIdScreen.route){
                    StudentSignUpIdScreen(navController = navController)
                }
                composable(
                    route = SignUpScreen.StudentPwScreen.route + "/{username}",
                    arguments = listOf(navArgument("username"){ type = NavType.StringType })
                ){
                    StudentSignUpPwScreen(
                        navController = navController,
                        username = it.arguments?.getString("username")!!
                    )
                }
                composable(
                    route = SignUpScreen.StudentInfoScreen.route + "?username={username}&password={password}",
                    arguments = listOf(
                        navArgument("username"){ type = NavType.StringType },
                        navArgument("password"){ type = NavType.StringType }
                    )
                ){
                    StudentSignUpInfoScreen(
                        username = it.arguments?.getString("username")!!,
                        password = it.arguments?.getString("password")!!
                    )
                }
                composable(route = SignUpScreen.TeacherIdScreen.route){
                    TeacherSignUpIdScreen(navController = navController)
                }
                composable(
                    route = SignUpScreen.TeacherPwScreen.route + "/{username}",
                    arguments = listOf(navArgument("username"){ type = NavType.StringType })
                ){
                    TeacherSignUpPwScreen(
                        navController = navController,
                        username = it.arguments?.getString("username")!!
                    )
                }
                composable(
                    route = SignUpScreen.TeacherInfoScreen.route + "?username={username}&password={password}",
                    arguments = listOf(
                        navArgument("username"){ type = NavType.StringType },
                        navArgument("password"){ type = NavType.StringType }
                    )
                ){
                    TeacherSignUpInfoScreen(
                        username = it.arguments?.getString("username")!!,
                        password = it.arguments?.getString("password")!!
                    )
                }
            }
        }
    }
}