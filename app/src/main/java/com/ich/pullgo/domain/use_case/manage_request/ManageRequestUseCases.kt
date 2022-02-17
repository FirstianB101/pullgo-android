package com.ich.pullgo.domain.use_case.manage_request

data class ManageRequestUseCases(
    val getAcademiesStudentApplying: GetAcademiesStudentApplyingUseCase,
    val getAcademiesTeacherApplying: GetAcademiesTeacherApplyingUseCase,
    val getClassroomsStudentApplying: GetClassroomsStudentApplyingUseCase,
    val getClassroomsTeacherApplying: GetClassroomsTeacherApplyingUseCase,
    val removeStudentApplyingAcademyRequest: RemoveStudentAcademyRequestUseCase,
    val removeStudentApplyingClassroomRequest: RemoveStudentClassroomRequestUseCase,
    val removeTeacherApplyingAcademyRequest: RemoveTeacherAcademyRequestUseCase,
    val removeTeacherApplyingClassroomRequest: RemoveTeacherClassroomRequestUseCase,
    val getOwnerOfAcademy: GetOwnerOfAcademyUseCase,
    val getAcademyOfClassroom: GetAcademyOfClassroomUseCase
)