package com.harry.pullgo.data.objects

import java.io.Serializable

class Lesson(
    var name: String?,
    var classroomId: Long?,
    var schedule: Schedule?
):Serializable {
    var id: Long? = null
}

class Schedule(
    var date: String?,
    var beginTime: String?,
    var endTime: String?
):Serializable{

}