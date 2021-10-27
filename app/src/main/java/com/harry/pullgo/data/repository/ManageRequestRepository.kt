package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService

class ManageRequestRepository(token: String?) {
    private val manageRequestClient = RetrofitClient.getApiService(RetrofitService::class.java, token)

    suspend fun getTeacherApplyingAcademies(teacherId: Long) = manageRequestClient.getTeacherApplyingAcademies(teacherId)
    suspend fun getTeacherApplyingClassrooms(teacherId: Long) = manageRequestClient.getTeacherApplyingClassrooms(teacherId)
    suspend fun getStudentApplyingAcademies(studentId: Long) = manageRequestClient.getStudentApplyingAcademies(studentId)
    suspend fun getStudentApplyingClassrooms(studentId: Long) = manageRequestClient.getStudentApplyingClassrooms(studentId)

    fun removeTeacherAppliedAcademy(teacherId: Long, academyId: Long) = manageRequestClient.removeTeacherAppliedAcademy(teacherId, academyId)
    fun removeTeacherAppliedClassroom(teacherId: Long, classroomId: Long) = manageRequestClient.removeTeacherAppliedClassroom(teacherId, classroomId)
    fun removeStudentAppliedAcademy(studentId: Long, academyId: Long) = manageRequestClient.removeStudentAppliedAcademy(studentId, academyId)
    fun removeStudentAppliedClassroom(studentId: Long, classroomId: Long) = manageRequestClient.removeStudentAppliedClassroom(studentId, classroomId)
}