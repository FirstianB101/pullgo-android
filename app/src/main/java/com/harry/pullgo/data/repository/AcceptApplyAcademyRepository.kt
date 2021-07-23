package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class AcceptApplyAcademyRepository {
    private val acceptClient = RetrofitClient.getApiService()

    suspend fun getStudentsAppliedAcademy(id: Long) = acceptClient.getStudentsRequestApplyAcademy(id,"schoolYear")
    suspend fun getTeachersAppliedAcademy(id: Long) = acceptClient.getTeachersRequestApplyAcademy(id)
    suspend fun getTeachersAcademies(id: Long) = acceptClient.getAcademiesTeacherApplied(id)
}