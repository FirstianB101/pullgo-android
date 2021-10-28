package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(
    private val signUpClient: PullgoService
) {
    fun createStudent(student: Student) = signUpClient.createStudent(student)
    fun createTeacher(teacher: Teacher) = signUpClient.createTeacher(teacher)

    suspend fun studentUsernameExists(username: String) = signUpClient.studentUsernameExists(username)
    suspend fun teacherUsernameExists(username: String) = signUpClient.teacherUsernameExists(username)
}