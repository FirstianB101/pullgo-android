package com.ich.pullgo.presentation.main.teacher_main.manage_academy

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class ManageAcademyState(
    val isLoading: Boolean = false,
    val ownedAcademies: List<Academy> = emptyList(),
    val selectedAcademy: Academy? = null,
    val academyAddress: String = "",
    val academyPhone: String = ""
)