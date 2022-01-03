package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import javax.inject.Inject

class ManageAcademyRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getAcademiesByOwnerId(teacherId: Long) = api.getOwnedAcademy(teacherId)
    suspend fun getTeachersSuchAcademy(academyId: Long) = api.getTeachersSuchAcademy(academyId)
    suspend fun getStudentsSuchAcademy(academyId: Long) = api.getStudentsSuchAcademy(academyId)

    suspend fun kickStudent(academyId: Long, studentId: Long) = api.kickStudent(academyId,studentId)
    suspend fun kickTeacher(academyId: Long, teacherId: Long) = api.kickTeacher(academyId,teacherId)
    suspend fun editAcademy(academyId: Long, academy: Academy) = api.editAcademy(academyId, academy)
    suspend fun deleteAcademy(academyId: Long) = api.deleteAcademy(academyId)

}