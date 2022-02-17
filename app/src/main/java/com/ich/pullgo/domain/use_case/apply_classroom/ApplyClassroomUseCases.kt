package com.ich.pullgo.domain.use_case.apply_classroom

data class ApplyClassroomUseCases(
    val getStudentAppliedAcademies: GetStudentAppliedAcademiesUseCase,
    val getTeacherAppliedAcademies: GetTeacherAppliedAcademiesUseCase,
    val getSearchedClassrooms: GetSearchedClassroomsUseCase,
    val sendStudentApplyClassroomRequest: SendStudentApplyClassroomRequestUseCase,
    val sendTeacherApplyClassroomRequest: SendTeacherApplyClassroomRequestUseCase
)