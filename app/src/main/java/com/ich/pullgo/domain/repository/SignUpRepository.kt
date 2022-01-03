package com.ich.pullgo.domain.repository

import com.ich.pullgo.data.remote.dto.ExistDto
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

interface SignUpRepository {
    suspend fun createStudent(student: Student): Student
    suspend fun createTeacher(teacher: Teacher): Teacher

    suspend fun studentUsernameExists(username: String): ExistDto
    suspend fun teacherUsernameExists(username: String): ExistDto
}