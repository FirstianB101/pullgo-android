package com.ich.pullgo.data.models

import java.io.Serializable

class Academy(
    var name: String?,
    var phone: String?,
    var address: String?,
    var ownerId: Long?
) :Serializable {
    var id: Long? = null
    override fun toString(): String {
        return name ?: ""
    }
}