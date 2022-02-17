package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question

import com.ich.pullgo.domain.model.ImageUploadResponse
import com.ich.pullgo.domain.model.Question

sealed class ManageQuestionState {
    object Loading: ManageQuestionState()
    object Normal: ManageQuestionState()
    data class Error(val message: String): ManageQuestionState()
    data class CreateQuestion(val question: Question): ManageQuestionState()
    data class EditQuestion(val question: Question): ManageQuestionState()
    object DeleteQuestion: ManageQuestionState()
    data class GetQuestions(val questions: List<Question>): ManageQuestionState()
    data class UploadImage(val response: ImageUploadResponse): ManageQuestionState()
}