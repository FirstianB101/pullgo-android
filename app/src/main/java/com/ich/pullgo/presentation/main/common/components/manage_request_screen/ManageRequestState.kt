package com.ich.pullgo.presentation.main.common.components.manage_request_screen

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Teacher

data class ManageRequestState(
    val isLoading: Boolean = false,
    val academyRequests: List<Academy> = emptyList(),
    val classroomRequests: List<Classroom> = emptyList(),
    val academyOwner: Teacher? = null,
    val academyInfo: Academy? = null,
    val selectedAcademy: Academy? = null,
    val selectedClassroom: Classroom? = null
)