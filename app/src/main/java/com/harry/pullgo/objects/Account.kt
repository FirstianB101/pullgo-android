package com.harry.pullgo.objects

import java.io.Serializable

class Account :Serializable{
    var username:String?
    var fullName:String?
    var phone:String?
    constructor(userName:String?, fullName:String?, phone:String?) {
        this.username = userName
        this.fullName = fullName
        this.phone = phone
    }
    constructor(source: Account) {
        this.username = source.username
        this.fullName = source.fullName
        this.phone = source.phone
    }
}