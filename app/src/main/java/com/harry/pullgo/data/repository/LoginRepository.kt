package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.objects.LoginInfo

class LoginRepository(context: Context) {
    private val loginClient = RetrofitClient.getApiService(RetrofitService::class.java, LoginInfo.user?.token,context)

    suspend fun getLoginUser(account: Account) = loginClient.getToken(account)
    suspend fun getAutoLoginUser() = loginClient.authorizeUser()
    suspend fun getAcademiesByStudentId(id: Long) = loginClient.getAcademiesStudentApplied(id)
    suspend fun getAcademiesByTeacherId(id: Long) = loginClient.getAcademiesTeacherApplied(id)
}