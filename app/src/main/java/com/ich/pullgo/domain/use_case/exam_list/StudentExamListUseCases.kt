package com.ich.pullgo.domain.use_case.exam_list

data class StudentExamListUseCases(
    val getExamsByName: GetExamsByNameUseCase,
    val getExamsByBeginDate: GetExamsByBeginDateUseCase,
    val getExamsByEndDate: GetExamsByEndDateUseCase,
    val getExamsByEndDateDesc: GetExamsByEndDateDescUseCase,
    val getStatesByStudentId: GetStatesByStudentIdUseCase,
    val startTakingExam: StartTakingExamUseCase
)
