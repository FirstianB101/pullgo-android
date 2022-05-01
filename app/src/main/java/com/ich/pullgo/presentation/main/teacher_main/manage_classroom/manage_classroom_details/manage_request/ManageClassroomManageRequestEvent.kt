package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ManageClassroomManageRequestEvent {
    data class GetStudentsRequests(val selectedClassroomId: Long): ManageClassroomManageRequestEvent()
    data class GetTeachersRequests(val selectedClassroomId: Long): ManageClassroomManageRequestEvent()
    data class SelectTeacherRequest(val teacher: Teacher): ManageClassroomManageRequestEvent()
    data class SelectStudentRequest(val student: Student): ManageClassroomManageRequestEvent()
    data class DenyTeacher(val selectedClassroomId: Long): ManageClassroomManageRequestEvent()
    data class AcceptTeacher(val selectedClassroomId: Long): ManageClassroomManageRequestEvent()
    data class DenyStudent(val selectedClassroomId: Long): ManageClassroomManageRequestEvent()
    data class AcceptStudent(val selectedClassroomId: Long): ManageClassroomManageRequestEvent()
}