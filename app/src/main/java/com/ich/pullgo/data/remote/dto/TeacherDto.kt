package com.ich.pullgo.data.remote.dto;

import com.ich.pullgo.domain.model.Teacher
import java.io.Serializable

class TeacherDto(
    var id:Long? = null,
    var account: AccountDto?
) : Serializable

fun TeacherDto.toTeacher(): Teacher {
    val teacher = Teacher(account?.toAccount())
    teacher.id = id
    return teacher
}