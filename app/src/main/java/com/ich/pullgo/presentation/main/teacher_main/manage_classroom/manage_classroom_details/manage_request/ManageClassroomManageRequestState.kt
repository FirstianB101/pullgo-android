package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ManageClassroomManageRequestState {
    object Loading: ManageClassroomManageRequestState()
    object Normal: ManageClassroomManageRequestState()
    data class Error(val message: String): ManageClassroomManageRequestState()
    data class GetStudentRequests(val students: List<Student>): ManageClassroomManageRequestState()
    data class GetTeacherRequests(val teachers: List<Teacher>): ManageClassroomManageRequestState()
    object AcceptRequest: ManageClassroomManageRequestState()
    object DenyRequest: ManageClassroomManageRequestState()
}