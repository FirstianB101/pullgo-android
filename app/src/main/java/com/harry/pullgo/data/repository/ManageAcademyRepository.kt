package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class ManageAcademyRepository {
    val client = RetrofitClient.getApiService()

    suspend fun getAcademiesByOwnerId(teacherId: Long) = client.getOwnedAcademy(teacherId)
    suspend fun getTeachersSuchAcademy(academyId: Long) = client.getTeachersSuchAcademy(academyId)
    suspend fun getStudentsSuchAcademy(academyId: Long) = client.getStudentsSuchAcademy(academyId)
}