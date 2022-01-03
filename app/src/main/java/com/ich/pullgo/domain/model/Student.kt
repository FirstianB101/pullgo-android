package com.ich.pullgo.domain.model

import java.io.Serializable

class Student(
    var account: Account?,
    var parentPhone: String?,
    var schoolName: String?,
    var schoolYear: Int?
) : Serializable{
    var id:Long?=null

    override fun toString(): String {
        return "${account?.fullName} (${account?.username})"
    }
}