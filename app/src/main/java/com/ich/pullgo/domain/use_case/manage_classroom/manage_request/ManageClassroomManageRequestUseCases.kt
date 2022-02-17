package com.ich.pullgo.domain.use_case.manage_classroom.manage_request

data class ManageClassroomManageRequestUseCases(
    val getStudentRequests: GetStudentClassroomRequestsUseCase,
    val getTeacherRequests: GetTeacherClassroomRequestsUseCase,
    val acceptStudent: AcceptStudentClassroomRequestUseCase,
    val acceptTeacher: AcceptTeacherClassroomRequestUseCase,
    val denyStudent: DenyStudentClassroomRequestUseCase,
    val denyTeacher: DenyTeacherClassroomRequestUseCase
)
