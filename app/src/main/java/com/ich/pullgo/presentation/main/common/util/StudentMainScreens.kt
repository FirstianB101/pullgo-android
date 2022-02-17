package com.ich.pullgo.presentation.main.common.util

import com.ich.pullgo.R

sealed class StudentMainScreens(val route: String, val title: String, val icon: Int){
    object HomeNoAcademy: StudentMainScreens("student_main_home_no_academy", "홈으로", R.drawable.baseline_home_24)
    object ChangeInfo: StudentMainScreens("student_main_change_info","회원정보 변경", R.drawable.change_info)
    object Calendar: StudentMainScreens("student_main_calendar","수업 일정", R.drawable.calendar)
    object ExamList: StudentMainScreens("student_main_exam_list", "시험 목록", R.drawable.quiz)
    object ExamHistory: StudentMainScreens("student_main_exam_history","오답 노트", R.drawable.outline_history_24)
    object ApplyAcademy: StudentMainScreens("student_main_apply_academy", "학원 가입 요청", R.drawable.school)
    object ApplyClassroom: StudentMainScreens("student_main_apply_classroom", "반 가입 요청", R.drawable.classroom)
    object ManageRequest: StudentMainScreens("student_main_manage_request", "보낸 요청 관리", R.drawable.manage_request)
}