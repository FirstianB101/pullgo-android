package com.ich.pullgo.presentation.sign_up.util

sealed class SignUpScreen(val route: String){
    object SignUpMainScreen: SignUpScreen("sign_up_main_screen")

    object StudentIdScreen: SignUpScreen("student_id_screen")
    object StudentPwScreen: SignUpScreen("student_pw_screen")
    object StudentInfoScreen: SignUpScreen("student_info_screen")

    object TeacherIdScreen: SignUpScreen("teacher_id_screen")
    object TeacherPwScreen: SignUpScreen("teacher_pw_screen")
    object TeacherInfoScreen: SignUpScreen("teacher_info_screen")
}