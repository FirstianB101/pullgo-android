package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TakeExamRepository @Inject constructor(
    private val client: PullgoService
) {
    suspend fun getOneQuestion(questionId: Long) = client.getOneQuestion(questionId)
    suspend fun getQuestionsSuchExam(examId: Long) = client.getQuestionsSuchExam(examId,100)
}