package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User

sealed class ChangeInfoState {
    object Loading: ChangeInfoState()
    object Normal: ChangeInfoState()
    data class PatchStudent(val student: Student): ChangeInfoState()
    data class PatchTeacher(val teacher: Teacher): ChangeInfoState()
    data class Error(val message: String): ChangeInfoState()
    data class AuthUser(val user: User): ChangeInfoState()
}