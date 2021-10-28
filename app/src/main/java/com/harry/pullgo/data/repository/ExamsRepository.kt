package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamsRepository @Inject constructor(
    private val examClient: PullgoService
) {
    suspend fun getExamsByBeginDate(studentId: Long) = examClient.getSortedStudentExams(studentId,"beginDateTime")
    suspend fun getExamsByEndDate(studentId: Long) = examClient.getSortedStudentExams(studentId,"endDateTime")
    suspend fun getExamsByName(studentId: Long) = examClient.getSortedStudentExams(studentId,"name")
    suspend fun getExamsByEndDateDesc(studentId: Long) = examClient.getStudentExamsDesc(studentId,"endDateTime")
}