package com.ich.pullgo.data.repository

import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toStudent
import com.ich.pullgo.data.remote.dto.toTeacher
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.repository.SignUpRepository
import javax.inject.Inject
import javax.inject.Singleton

class SignUpRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): SignUpRepository {
    override suspend fun createStudent(student: Student) = api.createStudent(student).toStudent()
    override suspend fun createTeacher(teacher: Teacher) = api.createTeacher(teacher).toTeacher()

    override suspend fun studentUsernameExists(username: String) = api.studentUsernameExists(username)
    override suspend fun teacherUsernameExists(username: String) = api.teacherUsernameExists(username)
}