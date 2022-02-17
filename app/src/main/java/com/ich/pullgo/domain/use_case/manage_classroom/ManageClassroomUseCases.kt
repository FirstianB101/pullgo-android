package com.ich.pullgo.domain.use_case.manage_classroom

data class ManageClassroomUseCases(
    val createClassroom: CreateClassroomUseCase,
    val getClassroomsTeacherApplied: GetClassroomsTeacherAppliedUseCase,
    val getAppliedAcademies: GetAppliedAcademiesUseCase
)