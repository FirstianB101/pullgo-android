package com.ich.pullgo.presentation.main.common.components.manage_request_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom

sealed class ManageRequestEvent {
    object GetAcademyRequests: ManageRequestEvent()
    object GetClassroomRequests: ManageRequestEvent()
    data class SelectAcademy(val academy: Academy): ManageRequestEvent()
    data class SelectClassroom(val classroom: Classroom): ManageRequestEvent()
    object RemoveAcademyRequest: ManageRequestEvent()
    object RemoveClassroomRequest: ManageRequestEvent()
    object ShowAcademyInfo: ManageRequestEvent()
    object ShowClassroomInfo: ManageRequestEvent()
}