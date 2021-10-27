package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher

class ChangeInfoRepository(token: String?) {
    private val changeInfoClient = RetrofitClient.getApiService(RetrofitService::class.java,token)

    fun changeStudentInfo(studentId: Long, student: Student) = changeInfoClient.changeStudentInfo(studentId, student)
    fun changeTeacherInfo(teacherId: Long, teacher: Teacher) = changeInfoClient.changeTeacherInfo(teacherId, teacher)
}