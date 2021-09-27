package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.objects.LoginInfo

class LoginRepository {
    private val loginClient = RetrofitClient.getApiService(RetrofitService::class.java, LoginInfo.user?.token)

    suspend fun getLoginUser(account: Account) = loginClient.getToken(account)
    suspend fun getAcademiesByStudentId(id: Long) = loginClient.getAcademiesStudentApplied(id)
    suspend fun getAcademiesByTeacherId(id: Long) = loginClient.getAcademiesTeacherApplied(id)
}