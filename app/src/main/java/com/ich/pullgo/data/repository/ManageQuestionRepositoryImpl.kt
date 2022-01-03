package com.ich.pullgo.data.repository

import com.ich.pullgo.common.Constants
import com.ich.pullgo.data.remote.ImageUploadApi
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.ImagebbRetrofitService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Question
import okhttp3.RequestBody
import javax.inject.Inject

class ManageQuestionRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi,
    @ImagebbRetrofitService private val imageApi: ImageUploadApi
){
    suspend fun createQuestion(question: Question) = api.createQuestion(question)
    suspend fun editQuestion(questionId: Long, question: Question) = api.editQuestion(questionId, question)
    suspend fun deleteQuestion(questionId: Long) = api.deleteQuestion(questionId)
    suspend fun getQuestionsSuchExam(examId: Long) = api.getQuestionsSuchExam(examId,100)

    suspend fun requestUploadImage(image: RequestBody) =
        imageApi.requestUpload(Constants.IMAGE_UPLOAD_API_KEY,image)
}