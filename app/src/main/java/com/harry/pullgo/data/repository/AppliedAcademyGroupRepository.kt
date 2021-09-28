package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.objects.LoginInfo

class AppliedAcademyGroupRepository(context: Context){
    private val appliedAcademyClient = RetrofitClient.getApiService(RetrofitService::class.java, LoginInfo.user?.token,context)

    suspend fun getStudentAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByStudentAppliedAcademyId(id)
    suspend fun getTeacherAppliedAcademies(id: Long) = appliedAcademyClient.getAcademiesByTeacherAppliedAcademyId(id)
}