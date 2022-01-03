package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject

class ExamHistoryRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getAttenderAnswers(attenderStateId: Long) = api.getAttenderAnswers(attenderStateId,100,"questionId")
    suspend fun getQuestionsSuchExam(examId: Long) = api.getQuestionsSuchExam(examId,100)
}