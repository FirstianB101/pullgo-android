package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.ImageUploadService
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.di.ImagebbRetrofitService
import com.ich.pullgo.di.NetworkModule
import com.ich.pullgo.di.PullgoRetrofitService
import okhttp3.RequestBody
import javax.inject.Inject

class ManageQuestionRepository @Inject constructor(
    @PullgoRetrofitService private val client: PullgoService,
    @ImagebbRetrofitService private val imageService: ImageUploadService
){
    suspend fun createQuestion(question: Question) = client.createQuestion(question)
    suspend fun editQuestion(questionId: Long, question: Question) = client.editQuestion(questionId, question)
    suspend fun deleteQuestion(questionId: Long) = client.deleteQuestion(questionId)
    suspend fun getQuestionsSuchExam(examId: Long) = client.getQuestionsSuchExam(examId,100)

    suspend fun requestUploadImage(image: RequestBody) =
        imageService.requestUpload(NetworkModule.IMAGE_UPLOAD_API_KEY,image)
}