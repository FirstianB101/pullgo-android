package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.User

interface LoginRepository {
    suspend fun getLoginUser(account: Account): User
    suspend fun getAutoLoginUser(): User
    suspend fun getAcademiesByStudentId(id: Long): List<Academy>
    suspend fun getAcademiesByTeacherId(id: Long): List<Academy>
}