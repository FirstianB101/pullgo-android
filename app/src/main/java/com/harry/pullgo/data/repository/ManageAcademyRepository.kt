package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.objects.LoginInfo

class ManageAcademyRepository(context: Context) {
    private val client = RetrofitClient.getApiService(RetrofitService::class.java, LoginInfo.user?.token,context)

    suspend fun getAcademiesByOwnerId(teacherId: Long) = client.getOwnedAcademy(teacherId)
    suspend fun getTeachersSuchAcademy(academyId: Long) = client.getTeachersSuchAcademy(academyId)
    suspend fun getStudentsSuchAcademy(academyId: Long) = client.getStudentsSuchAcademy(academyId)

    fun kickStudent(academyId: Long, studentId: Long) = client.kickStudent(academyId,studentId)
    fun kickTeacher(academyId: Long, teacherId: Long) = client.kickTeacher(academyId,teacherId)
    fun editAcademy(academyId: Long, academy: Academy) = client.editAcademy(academyId, academy)
    fun deleteAcademy(academyId: Long) = client.deleteAcademy(academyId)

}