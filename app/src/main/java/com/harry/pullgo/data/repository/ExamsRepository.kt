package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService

class ExamsRepository(token: String?) {
    private val examClient = RetrofitClient.getApiService(RetrofitService::class.java, token)

    suspend fun getExamsByBeginDate(studentId: Long) = examClient.getSortedStudentExams(studentId,"beginDateTime")
    suspend fun getExamsByEndDate(studentId: Long) = examClient.getSortedStudentExams(studentId,"endDateTime")
    suspend fun getExamsByName(studentId: Long) = examClient.getSortedStudentExams(studentId,"name")
    suspend fun getExamsByEndDateDesc(studentId: Long) = examClient.getStudentExamsDesc(studentId,"endDateTime")
}