package com.harry.pullgo.data.models

import java.io.Serializable

class Account(userName: String?, var fullName: String?, var phone: String?) :Serializable{
    var username:String? = userName
}