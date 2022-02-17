package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.ImageUploadResponse
import com.ich.pullgo.domain.model.Question
import okhttp3.RequestBody
import retrofit2.Response

interface ManageQuestionRepository {
    suspend fun createQuestion(question: Question): Question
    suspend fun editQuestion(questionId: Long, question: Question): Question
    suspend fun deleteQuestion(questionId: Long): Response<Unit>
    suspend fun getQuestionsSuchExam(examId: Long): List<Question>

    suspend fun requestUploadImage(image: RequestBody): ImageUploadResponse
}