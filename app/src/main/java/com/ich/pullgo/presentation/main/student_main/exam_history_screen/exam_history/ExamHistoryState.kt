package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history

import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question

sealed class ExamHistoryState{
    object Normal: ExamHistoryState()
    object Loading: ExamHistoryState()
    data class Error(val message: String): ExamHistoryState()
    data class GetQuestions(val questions: List<Question>): ExamHistoryState()
    data class GetAttenderAnswers(val answers: List<AttenderAnswer>): ExamHistoryState()
}
