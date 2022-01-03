package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/ExamHistoryRepository.kt
import com.ich.pullgo.data.api.PullgoService
=======
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/ExamHistoryRepository.kt
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExamHistoryRepository @Inject constructor(
    @PullgoRetrofitService private val client: PullgoService
) {
    suspend fun getAttenderAnswers(attenderStateId: Long) = client.getAttenderAnswers(attenderStateId,100,"questionId")
    suspend fun getQuestionsSuchExam(examId: Long) = client.getQuestionsSuchExam(examId,100)
}