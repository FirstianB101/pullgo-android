package com.ich.pullgo.domain.model

import kotlinx.coroutines.Job
import java.io.Serializable

class User : Serializable{
    var student: Student? = null
    var teacher: Teacher? = null
    var token: String? = null
}

fun User.doJob(
    ifStudent: () -> Job,
    ifTeacher: () -> Job
){
    when{
        student != null -> ifStudent()
        teacher != null -> ifTeacher()
    }
}