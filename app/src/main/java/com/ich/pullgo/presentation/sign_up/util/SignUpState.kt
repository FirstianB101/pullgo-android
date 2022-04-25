package com.ich.pullgo.presentation.sign_up.util

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class SignUpState(
    val createStudent: Student? = null,
    val createTeacher: Teacher? = null,
    val isLoading: Boolean = false,
    val idExist: Boolean? = null,
    val username: String = "",
    val password: String = "",
    val fullName: String = "",
    val phone: String = "",
    val parentPhone: String = "",
    val schoolName: String = "",
    val schoolYear: String = "1학년"
)