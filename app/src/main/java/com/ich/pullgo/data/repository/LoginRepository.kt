package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/LoginRepository.kt
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Account
import com.ich.pullgo.di.PullgoRetrofitService
=======
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Account
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/LoginRepository.kt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    @PullgoRetrofitService private val loginClient: PullgoService
) {
    suspend fun getLoginUser(account: Account) = loginClient.getToken(account)
    suspend fun getAutoLoginUser() = loginClient.authorizeUser()
    suspend fun getAcademiesByStudentId(id: Long) = loginClient.getAcademiesStudentApplied(id)
    suspend fun getAcademiesByTeacherId(id: Long) = loginClient.getAcademiesTeacherApplied(id)
}