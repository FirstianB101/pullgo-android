package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject

class ExamsRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getExamsByBeginDate(studentId: Long) = api.getSortedStudentExams(studentId,"beginDateTime",100)
    suspend fun getExamsByEndDate(studentId: Long) = api.getSortedStudentExams(studentId,"endDateTime",100)
    suspend fun getExamsByName(studentId: Long) = api.getSortedStudentExams(studentId,"name",100)
    suspend fun getExamsByEndDateDesc(studentId: Long) = api.getStudentExamsDesc(studentId,"endDateTime",100)

    suspend fun getStatesByStudentId(studentId: Long) = api.getStudentAttenderStates(studentId,100)
    suspend fun startTakingExam(attender: CreateAttender) = api.createAttenderState(attender)
}