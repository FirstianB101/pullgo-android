package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ChangeInfoEvent {
    data class CheckPassword(val inputPassword: String): ChangeInfoEvent()
    data class ChangeStudentInfo(val student: Student): ChangeInfoEvent()
    data class ChangeTeacherInfo(val teacher: Teacher): ChangeInfoEvent()
}