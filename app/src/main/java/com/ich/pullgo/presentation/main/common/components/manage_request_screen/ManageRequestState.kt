package com.ich.pullgo.presentation.main.common.components.manage_request_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Teacher

sealed class ManageRequestState {
    object Loading : ManageRequestState()
    object Normal : ManageRequestState()
    data class Error(val message: String) : ManageRequestState()
    data class AcademyRequests(val requests: List<Academy>): ManageRequestState()
    data class ClassroomRequests(val requests: List<Classroom>): ManageRequestState()
    data class GetAcademyOwner(val owner: Teacher): ManageRequestState()
    data class GetAcademyOfClassroom(val academy: Academy): ManageRequestState()
    object RemoveAcademyRequest: ManageRequestState()
    object RemoveClassroomRequest: ManageRequestState()
}