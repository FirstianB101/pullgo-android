package com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam

import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Question

sealed class TakeExamState{
    object Normal: TakeExamState()
    object Loading: TakeExamState()
    data class Error(val message: String): TakeExamState()
    data class GetAnAttenderState(val attenderState: AttenderState): TakeExamState()
    data class GetQuestions(val questions: List<Question>): TakeExamState()
    data class GetAttenderAnswers(val answers: List<AttenderAnswer>): TakeExamState()
    data class SaveAttenderAnswer(val answer: AttenderAnswer): TakeExamState()
    object SubmitAttenderState: TakeExamState()
}
