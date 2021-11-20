package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.CreateAttender
import com.harry.pullgo.di.PullgoRetrofitService
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
    suspend fun getStatesByExamId(examId: Long) = examClient.getExamAttenderStates(examId,100)
    suspend fun startTakingExam(attender: CreateAttender) = examClient.createAttenderState(attender)
    suspend fun submitExam(stateId: Long) = examClient.submitAttenderState(stateId)
}