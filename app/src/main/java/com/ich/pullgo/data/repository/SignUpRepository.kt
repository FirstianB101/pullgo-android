package com.ich.pullgo.data.repository

<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/repository/SignUpRepository.kt
import com.ich.pullgo.data.api.PullgoService
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.di.PullgoRetrofitService
=======
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.PullgoService
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
>>>>>>> ich:app/src/main/java/com/harry/pullgo/data/repository/SignUpRepository.kt
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