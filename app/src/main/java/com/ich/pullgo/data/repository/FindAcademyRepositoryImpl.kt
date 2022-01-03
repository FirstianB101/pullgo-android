package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import javax.inject.Inject

class FindAcademyRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getAcademies(name: String) = api.getAcademiesByName(name)

    suspend fun requestStudentApply(studentId: Long, academyId: Long) = api.sendStudentApplyAcademyRequest(studentId, academyId)
    suspend fun requestTeacherApply(teacherId: Long, academyId: Long) = api.sendTeacherApplyAcademyRequest(teacherId, academyId)
    suspend fun createAcademy(academy: Academy) = api.createAcademy(academy)
}