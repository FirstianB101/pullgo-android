package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient

class AppliedAcademyGroupRepository {
    private val appliedAcademyClient = RetrofitClient.getApiService()

    suspend fun getStudentAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByStudentAppliedAcademyId(id)
    suspend fun getTeacherAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByTeacherAppliedAcademyId(id)
}