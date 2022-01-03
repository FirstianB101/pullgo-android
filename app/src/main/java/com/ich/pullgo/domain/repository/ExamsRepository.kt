package com.ich.pullgo.domain.repository

import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam

interface ExamsRepository {
    suspend fun getExamsByBeginDate(studentId: Long): List<Exam>
    suspend fun getExamsByEndDate(studentId: Long): List<Exam>
    suspend fun getExamsByName(studentId: Long): List<Exam>
    suspend fun getExamsByEndDateDesc(studentId: Long) : List<Exam>

    suspend fun getStatesByStudentId(studentId: Long): List<AttenderState>
    suspend fun startTakingExam(attender: CreateAttender): AttenderState
}