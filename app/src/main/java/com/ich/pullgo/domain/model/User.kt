package com.ich.pullgo.domain.model

import java.io.Serializable

class User : Serializable{
    var student: Student? = null
    var teacher: Teacher? = null
    var token: String? = null
}

fun User.doJob(
    ifStudent: () -> Unit,
    ifTeacher: () -> Unit
){
    when{
        student != null -> ifStudent()
        teacher != null -> ifTeacher()
    }
}