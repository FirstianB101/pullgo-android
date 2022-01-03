package com.ich.pullgo.domain.use_case.sign_up

data class SignUpUseCases(
    val createStudent: CreateStudentUseCase,
    val createTeacher: CreateTeacherUseCase,
    val checkIdExist: CheckIdExistUseCase
)