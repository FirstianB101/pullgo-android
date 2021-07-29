package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class LoginRepository {
    private val loginClient = RetrofitClient.getApiService()

    suspend fun getLoginStudent(id: Long) = loginClient.getStudent(id)
    suspend fun getLoginTeacher(id: Long) = loginClient.getTeacher(id)
    suspend fun getAcademiesByStudentId(id: Long) = loginClient.getAcademiesStudentApplied(id)
    suspend fun getAcademiesByTeacherId(id: Long) = loginClient.getAcademiesTeacherApplied(id)
}