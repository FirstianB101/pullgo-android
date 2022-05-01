package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

data class ManageClassroomManageRequestState(
    val isLoading: Boolean = false,
    val studentRequests: List<Student> = emptyList(),
    val selectedStudentRequest: Student? = null,
    val teacherRequests: List<Teacher> = emptyList(),
    val selectedTeacherRequest: Teacher? = null
)