package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class ChangeInfoState(
    val isLoading: Boolean = false,
    val isCorrectPw: Boolean = false,
    val editedTeacher: Teacher? = null,
    val editedStudent: Student? = null,
    val error: String = ""
)