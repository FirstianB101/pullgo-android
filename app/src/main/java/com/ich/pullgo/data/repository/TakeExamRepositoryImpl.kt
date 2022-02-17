package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAttenderAnswer
import com.ich.pullgo.data.remote.dto.toAttenderState
import com.ich.pullgo.data.remote.dto.toQuestion
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Answer
import com.ich.pullgo.domain.repository.TakeExamRepository
import javax.inject.Inject

class TakeExamRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): TakeExamRepository {
    override suspend fun getQuestionsSuchExam(examId: Long) = api.getQuestionsSuchExam(examId,100).map{q->q.toQuestion()}
    override suspend fun getOneAttenderState(attenderStateId: Long) = api.getOneAttenderState(attenderStateId).toAttenderState()
    override suspend fun submitAttenderState(attenderStateId: Long) = api.submitAttenderState(attenderStateId)

    override suspend fun saveAttenderAnswer(attenderStateId: Long, questionId: Long, answer: Answer)
        = api.saveAttenderAnswer(attenderStateId, questionId, answer).toAttenderAnswer()

    override suspend fun getAttenderAnswers(attenderStateId: Long)
        = api.getAttenderAnswers(attenderStateId,100,"questionId").map{a->a.toAttenderAnswer()}
}