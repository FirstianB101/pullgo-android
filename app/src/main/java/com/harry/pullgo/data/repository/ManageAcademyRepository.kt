package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Academy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageAcademyRepository @Inject constructor(
    private val client: PullgoService
) {
    suspend fun getAcademiesByOwnerId(teacherId: Long) = client.getOwnedAcademy(teacherId)
    suspend fun getTeachersSuchAcademy(academyId: Long) = client.getTeachersSuchAcademy(academyId)
    suspend fun getStudentsSuchAcademy(academyId: Long) = client.getStudentsSuchAcademy(academyId)

    fun kickStudent(academyId: Long, studentId: Long) = client.kickStudent(academyId,studentId)
    fun kickTeacher(academyId: Long, teacherId: Long) = client.kickTeacher(academyId,teacherId)
    fun editAcademy(academyId: Long, academy: Academy) = client.editAcademy(academyId, academy)
    fun deleteAcademy(academyId: Long) = client.deleteAcademy(academyId)

}