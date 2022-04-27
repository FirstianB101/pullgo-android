package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User

data class ChangeInfoState(
    val isLoading: Boolean = false,
    val patchedStudent: Student? = null,
    val patchedTeacher: Teacher? = null
)