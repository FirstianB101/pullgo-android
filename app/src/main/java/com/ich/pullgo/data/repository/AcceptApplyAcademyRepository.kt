package com.ich.pullgo.data.repository


import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcceptApplyAcademyRepository @Inject constructor(
    @PullgoRetrofitService private val acceptClient: PullgoService
) {
    suspend fun getStudentsAppliedAcademy(id: Long) = acceptClient.getStudentsRequestApplyAcademy(id,"schoolYear")
    suspend fun getTeachersAppliedAcademy(id: Long) = acceptClient.getTeachersRequestApplyAcademy(id)
    suspend fun getTeachersAcademies(id: Long) = acceptClient.getAcademiesTeacherApplied(id)

    suspend fun acceptStudentApply(academyId: Long, studentId: Long) = acceptClient.acceptStudentApplyAcademy(academyId, studentId)
    suspend fun acceptTeacherApply(academyId: Long, teacherId: Long) = acceptClient.acceptTeacherApplyAcademy(academyId, teacherId)
    suspend fun denyStudentApply(studentId: Long, academyId: Long) = acceptClient.removeStudentAcademyRequest(studentId, academyId)
    suspend fun denyTeacherApply(teacherId: Long, academyId: Long) = acceptClient.removeTeacherAcademyRequest(teacherId, academyId)
}