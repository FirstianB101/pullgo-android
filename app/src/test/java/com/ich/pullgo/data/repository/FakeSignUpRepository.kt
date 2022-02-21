package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.dto.ExistDto
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.SignUpRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeSignUpRepository: SignUpRepository {

    val students = mutableListOf(Student(Account("exist_student","","",""),"","",1))
    val teachers = mutableListOf(Teacher(Account("exist_teacher","","","")))

    override suspend fun createStudent(student: Student): Student {
        if(student.account?.fullName.isNullOrBlank() || student.account?.username.isNullOrBlank() ||
            student.account?.password.isNullOrBlank() || student.account?.phone.isNullOrBlank())
            throw HttpException(Response.error<ResponseBody>(500 ,ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
        else {
            students.add(student)
            return student
        }
    }

    override suspend fun createTeacher(teacher: Teacher): Teacher {
        if(teacher.account?.fullName.isNullOrBlank() || teacher.account?.username.isNullOrBlank() ||
            teacher.account?.password.isNullOrBlank() || teacher.account?.phone.isNullOrBlank())
            throw HttpException(Response.error<ResponseBody>(500 ,ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
        else {
            teachers.add(teacher)
            return teacher
        }
    }

    override suspend fun studentUsernameExists(username: String): ExistDto {
        for(student in students){
            if(student.account?.username == username){
                return ExistDto(exists = true)
            }
        }
        return ExistDto(exists = false)
    }

    override suspend fun teacherUsernameExists(username: String): ExistDto {
        for(teacher in teachers){
            if(teacher.account?.username == username){
                return ExistDto(exists = true)
            }
        }
        return ExistDto(exists = false)
    }
}