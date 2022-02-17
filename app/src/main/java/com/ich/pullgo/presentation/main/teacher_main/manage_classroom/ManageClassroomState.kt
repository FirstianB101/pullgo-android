package com.ich.pullgo.presentation.main.teacher_main.manage_classroom

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

sealed class ManageClassroomState{
    object Loading: ManageClassroomState()
    object Normal: ManageClassroomState()
    data class Error(val message: String): ManageClassroomState()
    data class GetClassrooms(val classrooms: List<Classroom>): ManageClassroomState()
    data class GetAcademies(val academies: List<Academy>): ManageClassroomState()
    data class CreateClassroom(val classroom: Classroom): ManageClassroomState()
}
