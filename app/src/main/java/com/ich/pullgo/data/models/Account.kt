package com.ich.pullgo.data.models

import java.io.Serializable

class Account(var username: String?, var fullName: String?, var phone: String?, var password: String?) :Serializable{
    var role: String? = null
}
