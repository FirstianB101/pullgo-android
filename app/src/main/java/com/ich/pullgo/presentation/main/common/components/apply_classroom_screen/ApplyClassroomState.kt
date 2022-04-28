package com.ich.pullgo.presentation.main.common.components.apply_classroom_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

data class ApplyClassroomState(
    val isLoading: Boolean = false,
    val appliedAcademies: List<Academy> = emptyList(),
    val searchedClassrooms: List<Classroom> = emptyList(),
    val searchQuery: String = "",
    val selectedAcademy: Academy? = null,
    val selectedClassroom: Classroom? = null
)