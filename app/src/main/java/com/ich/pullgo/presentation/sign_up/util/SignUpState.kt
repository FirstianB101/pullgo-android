package com.ich.pullgo.presentation.sign_up.util

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class SignUpState{
    object Loading: SignUpState()
    object Normal: SignUpState()
    data class Error(val message: String): SignUpState()
    data class CreateStudent(val student: Student): SignUpState()
    data class CreateTeacher(val Teacher: Teacher): SignUpState()
    data class CheckExist(val exist: Boolean): SignUpState()
}