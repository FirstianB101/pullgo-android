package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.CreateAttender
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamsRepository @Inject constructor(
    @PullgoRetrofitService private val examClient: PullgoService
) {
    suspend fun getExamsByBeginDate(studentId: Long) = examClient.getSortedStudentExams(studentId,"beginDateTime",100)
    suspend fun getExamsByEndDate(studentId: Long) = examClient.getSortedStudentExams(studentId,"endDateTime",100)
    suspend fun getExamsByName(studentId: Long) = examClient.getSortedStudentExams(studentId,"name",100)
    suspend fun getExamsByEndDateDesc(studentId: Long) = examClient.getStudentExamsDesc(studentId,"endDateTime",100)

    suspend fun getStatesByStudentId(studentId: Long) = examClient.getStudentAttenderStates(studentId,100)
    suspend fun startTakingExam(attender: CreateAttender) = examClient.createAttenderState(attender)
}