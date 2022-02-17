package com.ich.pullgo.domain.use_case.change_info

data class ChangeInfoUseCases(
    val changeStudentInfo: ChangeStudentInfoUseCase,
    val changeTeacherInfo: ChangeTeacherInfoUseCase,
    val checkUserPassword: PwCheckUseCase
)