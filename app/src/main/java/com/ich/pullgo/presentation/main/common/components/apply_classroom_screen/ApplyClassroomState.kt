package com.ich.pullgo.presentation.main.common.components.apply_classroom_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

sealed class ApplyClassroomState{
    object Loading: ApplyClassroomState()
    object Normal: ApplyClassroomState()
    data class Error(val message: String): ApplyClassroomState()
    data class AppliedAcademies(val academies: List<Academy>): ApplyClassroomState()
    data class SearchedClassrooms(val classrooms: List<Classroom>): ApplyClassroomState()
    object SendApplyClassroomRequest: ApplyClassroomState()
}