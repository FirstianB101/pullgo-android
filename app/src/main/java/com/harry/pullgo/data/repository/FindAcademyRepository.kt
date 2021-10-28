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

    fun requestStudentApply(studentId: Long, academyId: Long) = findAcademyClient.sendStudentApplyAcademyRequest(studentId, academyId)
    fun requestTeacherApply(teacherId: Long, academyId: Long) = findAcademyClient.sendTeacherApplyAcademyRequest(teacherId, academyId)
    fun createAcademy(academy: Academy) = findAcademyClient.createAcademy(academy)
}