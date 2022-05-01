package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom

import ca.antonious.materialdaypicker.MaterialDayPicker

sealed class ManageClassroomEditClassroomEvent {
    data class ClassroomNameChanged(val name: String): ManageClassroomEditClassroomEvent()
    data class ClassroomDaysChanged(val days: List<MaterialDayPicker.Weekday>): ManageClassroomEditClassroomEvent()
    data class EditClassroom(val classroomId: Long): ManageClassroomEditClassroomEvent()
    data class DeleteClassroom(val classroomId: Long): ManageClassroomEditClassroomEvent()
}