package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/ManageQuestionRepository.kt
import com.ich.pullgo.data.api.ImageUploadService
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.di.ImagebbRetrofitService
import com.ich.pullgo.di.NetworkModule
import com.ich.pullgo.di.PullgoRetrofitService
=======
import com.ich.pullgo.common.Constants
import com.ich.pullgo.data.remote.ImageUploadApi
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.di.ImagebbRetrofitService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Question
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/ManageQuestionRepository.kt
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