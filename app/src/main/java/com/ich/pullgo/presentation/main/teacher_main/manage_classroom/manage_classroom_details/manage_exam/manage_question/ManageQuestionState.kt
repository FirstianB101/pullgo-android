package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question

import com.ich.pullgo.domain.model.Question

data class ManageQuestionState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val answer: List<Int> = emptyList(),
    val choice: MutableMap<String,String> = mutableMapOf(),
    val pictureUrl: String? = null,
    val content: String = "",
    val questionId: Long = -1L
)