package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAttenderAnswer
import com.ich.pullgo.data.remote.dto.toQuestion
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.repository.ExamHistoryRepository
import javax.inject.Inject

class ExamHistoryRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ExamHistoryRepository {

    override suspend fun getAttenderAnswers(attenderStateId: Long)
        = api.getAttenderAnswers(attenderStateId,100,"questionId").map{a->a.toAttenderAnswer()}

    override suspend fun getQuestionsSuchExam(examId: Long) = api.getQuestionsSuchExam(examId,100).map{q->q.toQuestion()}
}