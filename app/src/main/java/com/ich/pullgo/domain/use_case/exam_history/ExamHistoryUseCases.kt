package com.ich.pullgo.domain.use_case.exam_history

data class ExamHistoryUseCases(
    val getQuestions: GetQuestionsForHistoryUseCase,
    val getAttenderAnswers: GetAttenderAnswersUseCase
)
