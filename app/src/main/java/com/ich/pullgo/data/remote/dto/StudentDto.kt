package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Student
import java.io.Serializable

class StudentDto(
    var id:Long? = null,
    var account: AccountDto?,
    var parentPhone: String?,
    var schoolName: String?,
    var schoolYear: Int?
) : Serializable

fun StudentDto.toStudent(): Student{
    val student = Student(account?.toAccount(),parentPhone, schoolName, schoolYear)
    student.id = id
    return student
}