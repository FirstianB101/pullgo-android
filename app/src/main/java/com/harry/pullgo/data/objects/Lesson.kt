package com.harry.pullgo.data.objects

import java.io.Serializable

class Lesson(
    var name: String?,
    var classroomId: Long?,
    var schedule: Schedule?
):Serializable {
}

class Schedule(
    var date: String?,
    var beginTime: String?,
    var endTime: String?
):Serializable{

}