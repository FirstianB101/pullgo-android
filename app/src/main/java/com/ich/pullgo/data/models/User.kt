package com.ich.pullgo.data.models

import java.io.Serializable

class User : Serializable{
    var student: Student? = null
    var teacher: Teacher? = null
    var token: String? = null
}