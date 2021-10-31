package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeInfoRepository @Inject constructor(
    private val changeInfoClient: PullgoService
) {
    suspend fun changeStudentInfo(studentId: Long, student: Student) = changeInfoClient.changeStudentInfo(studentId, student)
    suspend fun changeTeacherInfo(teacherId: Long, teacher: Teacher) = changeInfoClient.changeTeacherInfo(teacherId, teacher)
}