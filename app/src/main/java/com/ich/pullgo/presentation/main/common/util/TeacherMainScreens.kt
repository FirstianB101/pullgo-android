package com.ich.pullgo.presentation.main.common.util

import com.ich.pullgo.R

sealed class TeacherMainScreens(val route: String, val title: String, val icon: Int){
    object HomeNoAcademy: TeacherMainScreens("teacher_main_home_no_academy", "홈으로", R.drawable.baseline_home_24)
    object ChangeInfo: TeacherMainScreens("teacher_main_change_info", "회원정보 변경", R.drawable.change_info)
    object Calendar: TeacherMainScreens("teacher_main_calendar", "수업 일정", R.drawable.calendar)
    object ManageClassroom: TeacherMainScreens("teacher_main_manage_classroom", "반 관리", R.drawable.classroom)
    object ManageAcademy: TeacherMainScreens("teacher_main_manage_academy", "학원 관리", R.drawable.manage_academy)
    object ApplyClassroom: TeacherMainScreens("teacher_main_apply_classroom", "반 가입 요청", R.drawable.ic_class)
    object ManageRequest: TeacherMainScreens("teacher_main_manage_request", "보낸 요청 관리", R.drawable.manage_request)
    object ApplyAcademy: TeacherMainScreens("teacher_main_apply_academy", "학원 가입 및 생성", R.drawable.school)
    object AcceptApplyingAcademy: TeacherMainScreens("teacher_accept_applying_academy", "학원 가입 승인", R.drawable.accept_apply)
}