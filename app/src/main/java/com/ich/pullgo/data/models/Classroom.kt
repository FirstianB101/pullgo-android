package com.ich.pullgo.data.models

import java.io.Serializable

class Classroom(
    var academyId: Long?,
    var name: String?,
    var creatorId: Long?
):Serializable {
    var id: Long? = null
    var creator: Teacher? = null
    override fun toString(): String {
        return name?.split(';')?.get(0) ?: ""
    }
}