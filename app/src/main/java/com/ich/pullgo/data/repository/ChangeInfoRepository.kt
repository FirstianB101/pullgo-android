package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Account
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeInfoRepository @Inject constructor(
    @PullgoRetrofitService private val changeInfoClient: PullgoService
) {
    suspend fun changeStudentInfo(studentId: Long, student: Student) = changeInfoClient.changeStudentInfo(studentId, student)
    suspend fun changeTeacherInfo(teacherId: Long, teacher: Teacher) = changeInfoClient.changeTeacherInfo(teacherId, teacher)
    suspend fun authUser(account: Account) = changeInfoClient.getToken(account)
}