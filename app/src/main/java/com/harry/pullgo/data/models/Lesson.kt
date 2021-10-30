package com.harry.pullgo.data.models

import java.io.Serializable

class Lesson(
    var name: String?,
    var classroomId: Long?,
    var schedule: Schedule?,
    var academyId: Long?
):Serializable {
    var id: Long? = null
}

class Schedule(
    var date: String?,
    var beginTime: String?,
    var endTime: String?
):Serializable