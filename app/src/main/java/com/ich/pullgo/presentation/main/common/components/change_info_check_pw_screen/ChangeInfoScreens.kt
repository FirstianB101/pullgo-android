package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

sealed class ChangeInfoScreen(val route: String){
    object ChangeInfoCheckPwScreen: ChangeInfoScreen("change_info_check_pw_screen")
    object StudentChangeInfoScreen: ChangeInfoScreen("student_change_info_screen")
    object TeacherChangeInfoScreen: ChangeInfoScreen("teacher_change_info_screen")
}
