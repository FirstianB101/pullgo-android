package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.di.ImagebbRetrofitService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.common.Constants
import com.ich.pullgo.data.api.ImageUploadApi
import com.ich.pullgo.domain.model.Question
import okhttp3.RequestBody
import javax.inject.Inject

class ManageQuestionRepository @Inject constructor(
    @PullgoRetrofitService private val client: PullgoService,
    @ImagebbRetrofitService private val imageService: ImageUploadApi
){
    suspend fun createQuestion(question: Question) = client.createQuestion(question)
    suspend fun editQuestion(questionId: Long, question: Question) = client.editQuestion(questionId, question)
    suspend fun deleteQuestion(questionId: Long) = client.deleteQuestion(questionId)
    suspend fun getQuestionsSuchExam(examId: Long) = client.getQuestionsSuchExam(examId,100)

    suspend fun requestUploadImage(image: RequestBody) =
        imageService.requestUpload(Constants.IMAGE_UPLOAD_API_KEY,image)
}