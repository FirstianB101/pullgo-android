package com.ich.pullgo.presentation.main.teacher_main.manage_classroom

import ca.antonious.materialdaypicker.MaterialDayPicker
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

data class ManageClassroomState(
    val isLoading: Boolean = false,
    val appliedClassrooms: List<Classroom> = emptyList(),
    val selectedClassroom: Classroom? = null,
    val appliedAcademies: List<Academy> = emptyList(),
    val selectedAcademy: Academy? = null,
    val newClassroomName: String = "",
    val newClassroomDays: List<MaterialDayPicker.Weekday> = emptyList()
)