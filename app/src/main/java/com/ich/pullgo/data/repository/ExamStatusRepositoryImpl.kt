package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject

class ExamStatusRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getAttenderStatesInExam(examId: Long) = api.getExamAttenderStates(examId,100)
    suspend fun getOneStudent(studentId: Long) = api.getOneStudent(studentId)

    suspend fun getStudentsInClassroom(classroomId: Long) = api.getStudentsAppliedClassroom(classroomId)
}