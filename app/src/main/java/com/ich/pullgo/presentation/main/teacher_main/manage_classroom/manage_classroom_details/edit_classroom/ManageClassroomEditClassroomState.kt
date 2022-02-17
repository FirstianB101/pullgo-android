package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom

import com.ich.pullgo.domain.model.Classroom

sealed class ManageClassroomEditClassroomState {
    object Loading: ManageClassroomEditClassroomState()
    object Normal: ManageClassroomEditClassroomState()
    data class Error(val message: String): ManageClassroomEditClassroomState()
    data class EditClassroom(val classroom: Classroom): ManageClassroomEditClassroomState()
    object DeleteClassroom: ManageClassroomEditClassroomState()
}