package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question

import com.ich.pullgo.domain.model.Question

sealed class ManageQuestionEvent{
    data class SelectQuestion(val question: Question): ManageQuestionEvent()
    data class ContentChanged(val content: String): ManageQuestionEvent()
    data class ChoiceChanged(val questionNum: String, val choice: String): ManageQuestionEvent()
    data class AnswerChanged(val answer: List<Int>): ManageQuestionEvent()
    data class SetPictureUrl(val url: String): ManageQuestionEvent()
    object CreateQuestion: ManageQuestionEvent()
    object GetQuestionsInExam: ManageQuestionEvent()
    object DeleteQuestion: ManageQuestionEvent()
    object EditQuestion: ManageQuestionEvent()
}
