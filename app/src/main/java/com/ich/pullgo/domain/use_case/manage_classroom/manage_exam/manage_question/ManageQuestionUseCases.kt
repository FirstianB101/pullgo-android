package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.manage_question

data class ManageQuestionUseCases(
    val createQuestion: CreateQuestionUseCase,
    val deleteQuestion: DeleteQuestionUseCase,
    val editQuestion: EditQuestionUseCase,
    val getQuestions: GetQuestionsInExamUseCase,
    val uploadImage: UploadQuestionImageUseCase
)