package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom

import ca.antonious.materialdaypicker.MaterialDayPicker

data class ManageClassroomEditClassroomState(
    val isLoading: Boolean = false,
    val classroomName: String = "",
    val classroomDays: List<MaterialDayPicker.Weekday> = emptyList()
)