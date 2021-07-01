package com.harry.pullgo.objects;

import java.io.Serializable

class Teacher : Serializable{
    var id:Long? = null
    var account: Account? = null

    constructor(account: Account){
        this.account=account
    }

    override fun toString(): String {
        return "id:$id userName:${account?.username} fullName:${account?.fullName} phone:${account?.phone}"
    }

}
