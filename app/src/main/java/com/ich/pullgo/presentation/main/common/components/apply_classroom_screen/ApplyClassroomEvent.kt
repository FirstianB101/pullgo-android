package com.ich.pullgo.presentation.main.common.components.apply_classroom_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

sealed class ApplyClassroomEvent {
    data class SearchQueryChanged(val query: String): ApplyClassroomEvent()
    data class SelectAcademy(val academy: Academy): ApplyClassroomEvent()
    data class SelectClassroom(val classroom: Classroom): ApplyClassroomEvent()
    object RequestApplyingClassroom: ApplyClassroomEvent()
    object SearchClassrooms: ApplyClassroomEvent()
}