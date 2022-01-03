package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question

interface ExamHistoryRepository {
    suspend fun getAttenderAnswers(attenderStateId: Long): List<AttenderAnswer>
    suspend fun getQuestionsSuchExam(examId: Long): List<Question>
}