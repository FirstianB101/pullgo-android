package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.objects.LoginInfo

class FindAcademyRepository(context: Context) {
    private val findAcademyClient = RetrofitClient.getApiService(RetrofitService::class.java,LoginInfo.user?.token,context)

    suspend fun getAcademies(name: String) = findAcademyClient.getAcademiesByName(name)

    fun requestStudentApply(studentId: Long, academyId: Long) = findAcademyClient.sendStudentApplyAcademyRequest(studentId, academyId)
    fun requestTeacherApply(teacherId: Long, academyId: Long) = findAcademyClient.sendTeacherApplyAcademyRequest(teacherId, academyId)
    fun createAcademy(academy: Academy) = findAcademyClient.createAcademy(academy)
}