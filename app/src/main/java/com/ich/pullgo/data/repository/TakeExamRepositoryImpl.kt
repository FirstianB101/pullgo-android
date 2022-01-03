package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Answer
import javax.inject.Inject

class TakeExamRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getQuestionsSuchExam(examId: Long) = api.getQuestionsSuchExam(examId,100)
    suspend fun getOneAttenderState(attenderStateId: Long) = api.getOneAttenderState(attenderStateId)
    suspend fun submitAttenderState(attenderStateId: Long) = api.submitAttenderState(attenderStateId)

    suspend fun saveAttenderAnswer(attenderStateId: Long, questionId: Long, answer: Answer)
                                = api.saveAttenderAnswer(attenderStateId, questionId, answer)

    suspend fun getAttenderAnswers(attenderStateId: Long) = api.getAttenderAnswers(attenderStateId,100,"questionId")
}