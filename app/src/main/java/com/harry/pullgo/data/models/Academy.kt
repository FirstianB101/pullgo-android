package com.harry.pullgo.data.models

import java.io.Serializable

class Academy(
    var id: Long?,
    var name: String?,
    var phone: String?,
    var address: String?,
    var ownerId: Long?
) :Serializable {
    override fun toString(): String {
        return name ?: ""
    }
}