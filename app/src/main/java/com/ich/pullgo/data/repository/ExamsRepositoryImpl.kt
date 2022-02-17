package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.data.remote.dto.toAttenderState
import com.ich.pullgo.data.remote.dto.toExam
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.repository.ExamsRepository
import javax.inject.Inject

class ExamsRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ExamsRepository {
    override suspend fun getExamsByBeginDate(studentId: Long) = api.getSortedStudentExams(studentId,"beginDateTime",100).map{e->e.toExam()}
    override suspend fun getExamsByEndDate(studentId: Long) = api.getSortedStudentExams(studentId,"endDateTime",100).map{e->e.toExam()}
    override suspend fun getExamsByName(studentId: Long) = api.getSortedStudentExams(studentId,"name",100).map{e->e.toExam()}
    override suspend fun getExamsByEndDateDesc(studentId: Long) = api.getStudentExamsDesc(studentId,"endDateTime",100).map{e->e.toExam()}

    override suspend fun getStatesByStudentId(studentId: Long) = api.getStudentAttenderStates(studentId,100).map{s->s.toAttenderState()}
    override suspend fun startTakingExam(attender: CreateAttender) = api.createAttenderState(attender).toAttenderState()
}