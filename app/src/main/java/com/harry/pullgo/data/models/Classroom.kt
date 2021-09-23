package com.harry.pullgo.data.models

import java.io.Serializable

class Classroom(
    var academyId: Long?,
    var name: String?
):Serializable {
    var id: Long? = null
    var creatorId: Long? = null
    override fun toString(): String {
        return name?.split(';')?.get(0) ?: ""
    }
}