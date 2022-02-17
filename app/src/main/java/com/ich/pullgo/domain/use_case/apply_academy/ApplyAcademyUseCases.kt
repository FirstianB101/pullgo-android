package com.ich.pullgo.domain.use_case.apply_academy

data class ApplyAcademyUseCases(
    val createAcademy: CreateAcademyUseCase,
    val getSearchedAcademies: GetSearchedAcademiesUseCase,
    val studentApplyAcademy: StudentApplyAcademyUseCase,
    val teacherApplyAcademy: TeacherApplyAcademyUseCase
)