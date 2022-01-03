package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageAcademyRepository @Inject constructor(
    @PullgoRetrofitService private val client: PullgoService
) {
    suspend fun getAcademiesByOwnerId(teacherId: Long) = client.getOwnedAcademy(teacherId)
    suspend fun getTeachersSuchAcademy(academyId: Long) = client.getTeachersSuchAcademy(academyId)
    suspend fun getStudentsSuchAcademy(academyId: Long) = client.getStudentsSuchAcademy(academyId)

    suspend fun kickStudent(academyId: Long, studentId: Long) = client.kickStudent(academyId,studentId)
    suspend fun kickTeacher(academyId: Long, teacherId: Long) = client.kickTeacher(academyId,teacherId)
    suspend fun editAcademy(academyId: Long, academy: Academy) = client.editAcademy(academyId, academy)
    suspend fun deleteAcademy(academyId: Long) = client.deleteAcademy(academyId)

}