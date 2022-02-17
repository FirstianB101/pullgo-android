package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoScreen
import com.ich.pullgo.presentation.main.student_main.change_info_screen.components.StudentChangeInfoScreen
import com.ich.pullgo.presentation.main.teacher_main.change_info_screen.components.TeacherChangeInfoScreen

@Composable
fun ChangeInfoNavigation(
    isTeacher: Boolean
){
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = ChangeInfoScreen.ChangeInfoCheckPwScreen.route
    ) {
        composable(ChangeInfoScreen.ChangeInfoCheckPwScreen.route) {
            ChangeInfoCheckPwScreen(
                navController = navController,
                isTeacher = isTeacher
            )
        }
        composable(ChangeInfoScreen.StudentChangeInfoScreen.route) {
            StudentChangeInfoScreen(navController = navController)
        }
        composable(ChangeInfoScreen.TeacherChangeInfoScreen.route) {
            TeacherChangeInfoScreen(navController = navController)
        }
    }
}