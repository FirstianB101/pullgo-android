package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Answer
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Question
import retrofit2.Response

interface TakeExamRepository {
    suspend fun getQuestionsSuchExam(examId: Long): List<Question>
    suspend fun getOneAttenderState(attenderStateId: Long): AttenderState
    suspend fun submitAttenderState(attenderStateId: Long): Response<Unit>

    suspend fun saveAttenderAnswer(attenderStateId: Long, questionId: Long, answer: Answer): AttenderAnswer

    suspend fun getAttenderAnswers(attenderStateId: Long): List<AttenderAnswer>
}