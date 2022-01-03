package com.ich.pullgo.domain.model

import java.io.Serializable

class Teacher(var account: Account?) : Serializable{
    var id:Long? = null

    override fun toString(): String {
        return "${account?.fullName} (${account?.username})"
    }
}