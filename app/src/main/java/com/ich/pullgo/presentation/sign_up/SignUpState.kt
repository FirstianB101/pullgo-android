package com.ich.pullgo.presentation.sign_up

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class SignUpState(
    val isLoading: Boolean = false,
    val newStudent: Student? = null,
    val newTeacher: Teacher? = null,
    val exist: Boolean? = null,
    val error: String = ""
)