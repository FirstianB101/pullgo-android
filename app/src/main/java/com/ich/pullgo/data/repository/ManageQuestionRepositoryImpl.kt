package com.ich.pullgo.data.repository

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.data.remote.ImageUploadApi
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toImageUploadResponse
import com.ich.pullgo.data.remote.dto.toQuestion
import com.ich.pullgo.di.ImagebbRetrofitService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.domain.repository.ManageQuestionRepository
import okhttp3.RequestBody
import javax.inject.Inject

class ManageQuestionRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi,
    @ImagebbRetrofitService private val imageApi: ImageUploadApi
): ManageQuestionRepository {
    override suspend fun createQuestion(question: Question) = api.createQuestion(question).toQuestion()
    override suspend fun editQuestion(questionId: Long, question: Question) = api.editQuestion(questionId, question).toQuestion()
    override suspend fun deleteQuestion(questionId: Long) = api.deleteQuestion(questionId)
    override suspend fun getQuestionsSuchExam(examId: Long) = api.getQuestionsSuchExam(examId,100).map{q->q.toQuestion()}

    override suspend fun requestUploadImage(image: RequestBody) =
        imageApi.requestUpload(Constants.IMAGE_UPLOAD_API_KEY,image).toImageUploadResponse()
}