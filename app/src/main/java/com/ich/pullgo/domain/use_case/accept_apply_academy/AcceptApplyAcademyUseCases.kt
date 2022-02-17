package com.ich.pullgo.domain.use_case.accept_apply_academy

data class AcceptApplyAcademyUseCases(
    val getAcademiesTeacherApplied: GetTeacherAppliedAcademiesUseCase,
    val getTeachersApplyingAcademy: GetTeachersApplyingAcademyUseCase,
    val getStudentsApplyingAcademy: GetStudentsApplyingAcademyUseCase,
    val acceptTeacherRequest: AcceptTeacherApplyingAcademyUseCase,
    val acceptStudentRequest: AcceptStudentApplyingAcademyUseCase,
    val denyTeacherRequest: DenyTeacherApplyingAcademyUseCase,
    val denyStudentRequest: DenyStudentApplyingAcademyUseCase
)