package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Academy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindAcademyRepository @Inject constructor(
    private val findAcademyClient: PullgoService
) {
    suspend fun getAcademies(name: String) = findAcademyClient.getAcademiesByName(name)

    suspend fun requestStudentApply(studentId: Long, academyId: Long) = findAcademyClient.sendStudentApplyAcademyRequest(studentId, academyId)
    suspend fun requestTeacherApply(teacherId: Long, academyId: Long) = findAcademyClient.sendTeacherApplyAcademyRequest(teacherId, academyId)
    suspend fun createAcademy(academy: Academy) = findAcademyClient.createAcademy(academy)
}