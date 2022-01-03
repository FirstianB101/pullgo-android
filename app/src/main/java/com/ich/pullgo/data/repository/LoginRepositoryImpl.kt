package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.data.remote.dto.toUser
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val loginClient: PullgoApi
): LoginRepository{
    override suspend fun getLoginUser(account: Account) = loginClient.getToken(account).toUser()
    override suspend fun getAutoLoginUser() = loginClient.authorizeUser().toUser()
    override suspend fun getAcademiesByStudentId(id: Long) = loginClient.getAcademiesStudentApplied(id).map{it.toAcademy()}
    override suspend fun getAcademiesByTeacherId(id: Long) = loginClient.getAcademiesTeacherApplied(id).map{it.toAcademy()}
}