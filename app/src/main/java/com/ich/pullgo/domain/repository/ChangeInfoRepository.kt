package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User

interface ChangeInfoRepository{
    suspend fun changeStudentInfo(studentId: Long, student: Student): Student
    suspend fun changeTeacherInfo(teacherId: Long, teacher: Teacher): Teacher
    suspend fun authUser(account: Account): User
}