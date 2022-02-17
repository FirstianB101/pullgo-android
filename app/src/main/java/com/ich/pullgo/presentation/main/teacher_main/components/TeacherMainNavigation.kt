package com.ich.pullgo.presentation.main.teacher_main.components

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
import com.ich.pullgo.presentation.main.common.util.TeacherMainScreens
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.components.TeacherAcceptApplyAcademyScreen
import com.ich.pullgo.presentation.main.teacher_main.home_screen.TeacherHomeScreen
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.components.TeacherManageAcademyScreen
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.components.TeacherManageClassroomScreen


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TeacherMainNavigation(
    navController: NavHostController,
    academyExist: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if(academyExist) TeacherMainScreens.Calendar.route
        else TeacherMainScreens.HomeNoAcademy.route
    ) {
        composable(TeacherMainScreens.HomeNoAcademy.route) {
            TeacherHomeScreen(navController = navController)
        }
        composable(TeacherMainScreens.Calendar.route) {
            CalendarScreen()
        }
        composable(TeacherMainScreens.ChangeInfo.route) {
            ChangeInfoNavigation(isTeacher = true)
        }
        composable(TeacherMainScreens.ManageClassroom.route) {
            TeacherManageClassroomScreen()
        }
        composable(TeacherMainScreens.ManageAcademy.route) {
            TeacherManageAcademyScreen()
        }
        composable(TeacherMainScreens.ManageRequest.route) {
            ManageRequestScreen()
        }
        composable(TeacherMainScreens.ApplyAcademy.route) {
            ApplyAcademyScreen(isTeacher = true)
        }
        composable(TeacherMainScreens.ApplyClassroom.route) {
            ApplyClassroomScreen()
        }
        composable(TeacherMainScreens.AcceptApplyingAcademy.route) {
            TeacherAcceptApplyAcademyScreen()
        }
    }
}