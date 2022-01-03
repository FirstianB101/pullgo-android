package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/TakeExamRepository.kt
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Answer
import com.ich.pullgo.di.PullgoRetrofitService
=======
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Answer
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/TakeExamRepository.kt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TakeExamRepository @Inject constructor(
    @PullgoRetrofitService private val client: PullgoService
) {
    suspend fun getQuestionsSuchExam(examId: Long) = client.getQuestionsSuchExam(examId,100)
    suspend fun getOneAttenderState(attenderStateId: Long) = client.getOneAttenderState(attenderStateId)
    suspend fun submitAttenderState(attenderStateId: Long) = client.submitAttenderState(attenderStateId)

    suspend fun saveAttenderAnswer(attenderStateId: Long, questionId: Long, answer: Answer)
                                = client.saveAttenderAnswer(attenderStateId, questionId, answer)

    suspend fun getAttenderAnswers(attenderStateId: Long) = client.getAttenderAnswers(attenderStateId,100,"questionId")
}