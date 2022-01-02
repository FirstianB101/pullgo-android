package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamStatusRepository @Inject constructor(
    @PullgoRetrofitService private val client: PullgoService
) {
    suspend fun getAttenderStatesInExam(examId: Long) = client.getExamAttenderStates(examId,100)
    suspend fun getOneStudent(studentId: Long) = client.getOneStudent(studentId)

    suspend fun getStudentsInClassroom(classroomId: Long) = client.getStudentsAppliedClassroom(classroomId)
}