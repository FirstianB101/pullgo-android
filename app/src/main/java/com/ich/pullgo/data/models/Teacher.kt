package com.ich.pullgo.data.models;

import java.io.Serializable

class Teacher(account: Account) : Serializable{
    var id:Long? = null
    var account: Account? = account

    override fun toString(): String {
        return "${account?.fullName} (${account?.username})"
    }
}