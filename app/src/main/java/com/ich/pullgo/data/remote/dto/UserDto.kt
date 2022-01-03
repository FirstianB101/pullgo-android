package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.User
import java.io.Serializable

class UserDto : Serializable{
    var student: StudentDto? = null
    var teacher: TeacherDto? = null
    var token: String? = null
}

fun UserDto.toUser(): User{
    val user = User()
    user.student = student?.toStudent()
    user.teacher = teacher?.toTeacher()
    user.token = token
    return user
}