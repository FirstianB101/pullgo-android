package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Account
import java.io.Serializable

class AccountDto(
    var username: String?,
    var fullName: String?,
    var phone: String?,
    var password: String?,
    var role: String? = null
) :Serializable

fun AccountDto.toAccount(): Account{
    val account = Account(username, fullName, phone, password)
    account.role = role
    return account
}