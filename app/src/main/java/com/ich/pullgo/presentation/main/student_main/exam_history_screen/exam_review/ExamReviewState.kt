package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review

import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question

sealed class ExamReviewState{
    object Normal: ExamReviewState()
    object Loading: ExamReviewState()
    data class Error(val message: String): ExamReviewState()
    data class GetQuestions(val questions: List<Question>): ExamReviewState()
    data class GetAttenderAnswers(val answers: List<AttenderAnswer>): ExamReviewState()
}
