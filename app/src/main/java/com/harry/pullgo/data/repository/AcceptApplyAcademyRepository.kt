package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import retrofit2.Call

class AcceptApplyAcademyRepository {
    private val acceptClient = RetrofitClient.getApiService()

    suspend fun getStudentsAppliedAcademy(id: Long) = acceptClient.getStudentsAppliedAcademy(id,"schoolYear")
    suspend fun getTeachersAppliedAcademy(id: Long) = acceptClient.getTeachersAppliedAcademy(id)
    suspend fun getTeachersAcademies(id: Long) = acceptClient.getAcademiesTeacherApplied(id)
}