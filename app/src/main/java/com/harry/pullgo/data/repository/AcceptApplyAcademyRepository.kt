package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.objects.LoginInfo

class AcceptApplyAcademyRepository(context: Context) {
    private val acceptClient = RetrofitClient.getApiService(RetrofitService::class.java,LoginInfo.user?.token,context)

    suspend fun getStudentsAppliedAcademy(id: Long) = acceptClient.getStudentsRequestApplyAcademy(id,"schoolYear")
    suspend fun getTeachersAppliedAcademy(id: Long) = acceptClient.getTeachersRequestApplyAcademy(id)
    suspend fun getTeachersAcademies(id: Long) = acceptClient.getAcademiesTeacherApplied(id)

    fun acceptStudentApply(academyId: Long, studentId: Long) = acceptClient.acceptStudentApplyAcademy(academyId, studentId)
    fun acceptTeacherApply(academyId: Long, teacherId: Long) = acceptClient.acceptTeacherApplyAcademy(academyId, teacherId)
    fun denyStudentApply(studentId: Long, academyId: Long) = acceptClient.removeStudentAcademyRequest(studentId, academyId)
    fun denyTeacherApply(teacherId: Long, academyId: Long) = acceptClient.removeTeacherAcademyRequest(teacherId, academyId)
}