package com.harry.pullgo.data.objects

import java.io.Serializable

class Classroom(
    var id: Long?,
    var academyId: Long?,
    var name: String?
):Serializable {
    override fun toString(): String {
        return name?.split(';')?.get(0) ?: ""
    }
}

class MakeClassroom(
    var name: String?,
    var academyId: Long?,
    var creatorId: Long?
):Serializable