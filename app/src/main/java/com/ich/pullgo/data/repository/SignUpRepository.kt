package com.ich.pullgo.data.repository

import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.di.PullgoRetrofitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(
    @PullgoRetrofitService private val signUpClient: PullgoService
) {
    suspend fun createStudent(student: Student) = signUpClient.createStudent(student)
    suspend fun createTeacher(teacher: Teacher) = signUpClient.createTeacher(teacher)

    suspend fun studentUsernameExists(username: String) = signUpClient.studentUsernameExists(username)
    suspend fun teacherUsernameExists(username: String) = signUpClient.teacherUsernameExists(username)
}