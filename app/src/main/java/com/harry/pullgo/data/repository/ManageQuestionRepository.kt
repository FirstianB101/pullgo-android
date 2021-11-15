package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Question
import javax.inject.Inject

class ManageQuestionRepository @Inject constructor(
    private val client: PullgoService
){
    suspend fun createQuestion(question: Question) = client.createQuestion(question)
    suspend fun editQuestion(questionId: Long, question: Question) = client.editQuestion(questionId, question)
    suspend fun deleteQuestion(questionId: Long) = client.deleteQuestion(questionId)
    suspend fun getQuestionsSuchExam(examId: Long) = client.getQuestionsSuchExam(examId,100)
}