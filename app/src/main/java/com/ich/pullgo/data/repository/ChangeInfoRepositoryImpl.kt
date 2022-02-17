package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toStudent
import com.ich.pullgo.data.remote.dto.toTeacher
import com.ich.pullgo.data.remote.dto.toUser
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.ChangeInfoRepository
import javax.inject.Inject

class ChangeInfoRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ChangeInfoRepository {
    override suspend fun changeStudentInfo(studentId: Long, student: Student) = api.changeStudentInfo(studentId, student).toStudent()
    override suspend fun changeTeacherInfo(teacherId: Long, teacher: Teacher) = api.changeTeacherInfo(teacherId, teacher).toTeacher()
    override suspend fun authUser(account: Account) = api.getToken(account).toUser()
}