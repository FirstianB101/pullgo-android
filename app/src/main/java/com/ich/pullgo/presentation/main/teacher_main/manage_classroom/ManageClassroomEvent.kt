package com.ich.pullgo.presentation.main.teacher_main.manage_classroom

import ca.antonious.materialdaypicker.MaterialDayPicker
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

sealed class ManageClassroomEvent {
    data class SelectClassroom(val classroom: Classroom): ManageClassroomEvent()
    data class SelectAcademy(val academy: Academy): ManageClassroomEvent()
    data class NewClassroomNameChanged(val name: String): ManageClassroomEvent()
    data class NewClassroomWeekdayChanged(val weekday: List<MaterialDayPicker.Weekday>): ManageClassroomEvent()
    object GetAppliedAcademies: ManageClassroomEvent()
    object ResetClassroomList: ManageClassroomEvent()
    object CreateClassroom: ManageClassroomEvent()
}