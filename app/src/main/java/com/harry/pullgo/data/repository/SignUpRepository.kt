package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher

class SignUpRepository {
    private val signUpClient = RetrofitClient.getApiService()

    fun createStudent(student: Student) = signUpClient.createStudent(student)
    fun createTeacher(teacher: Teacher) = signUpClient.createTeacher(teacher)
}