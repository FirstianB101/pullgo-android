package com.ich.pullgo.domain.use_case.take_exam

data class TakeExamUseCases(
    val getAnAttenderState: GetAnAttenderStateUseCase,
    val getAttenderAnswers: GetAttenderAnswersUseCase,
    val getQuestionsInExam: GetQuestionsForExamUseCase,
    val saveAttenderAnswer: SaveAttenderAnswerUseCase,
    val submitAttenderState: SubmitAttenderStateUseCase
)
