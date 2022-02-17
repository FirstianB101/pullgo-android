package com.ich.pullgo.presentation.main.student_main.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ich.pullgo.presentation.main.common.components.apply_academy_screen.components.ApplyAcademyScreen
import com.ich.pullgo.presentation.main.common.components.apply_classroom_screen.components.ApplyClassroomScreen
import com.ich.pullgo.presentation.main.common.components.calendar_screen.components.CalendarScreen
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.components.ChangeInfoNavigation
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.components.ManageRequestScreen
import com.ich.pullgo.presentation.main.common.util.StudentMainScreens
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.components.StudentExamHistoryScreen
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.components.StudentExamListScreen
import com.ich.pullgo.presentation.main.student_main.home_screen.components.StudentHomeScreen

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun StudentMainNavigation(
    navController: NavHostController,
    academyExist: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if(academyExist) StudentMainScreens.Calendar.route
        else StudentMainScreens.HomeNoAcademy.route
    ) {
        composable(StudentMainScreens.HomeNoAcademy.route) {
            StudentHomeScreen(navController = navController)
        }
        composable(StudentMainScreens.Calendar.route) {
            CalendarScreen()
        }
        composable(StudentMainScreens.ChangeInfo.route) {
            ChangeInfoNavigation(isTeacher = false)
        }
        composable(StudentMainScreens.ExamHistory.route) {
            StudentExamHistoryScreen()
        }
        composable(StudentMainScreens.ExamList.route) {
            StudentExamListScreen()
        }
        composable(StudentMainScreens.ManageRequest.route) {
            ManageRequestScreen()
        }
        composable(StudentMainScreens.ApplyAcademy.route) {
            ApplyAcademyScreen(isTeacher = false)
        }
        composable(StudentMainScreens.ApplyClassroom.route) {
            ApplyClassroomScreen()
        }
    }
}