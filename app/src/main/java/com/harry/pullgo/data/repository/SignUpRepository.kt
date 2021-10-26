package com.harry.pullgo.data.repository

import android.content.Context
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.RetrofitService
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.objects.LoginInfo

class SignUpRepository(context: Context) {
    private val signUpClient = RetrofitClient.getApiService(RetrofitService::class.java, LoginInfo.user?.token,context)

    fun createStudent(student: Student) = signUpClient.createStudent(student)
    fun createTeacher(teacher: Teacher) = signUpClient.createTeacher(teacher)

    suspend fun studentUsernameExists(username: String) = signUpClient.studentUsernameExists(username)
    suspend fun teacherUsernameExists(username: String) = signUpClient.teacherUsernameExists(username)
}