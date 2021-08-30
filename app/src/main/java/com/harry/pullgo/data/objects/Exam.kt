package com.harry.pullgo.data.objects

import java.io.Serializable

class Exam (
    var id: Long?,
    var classroomId: Long?,
    var creatorId: Long?,
    var name: String?,
    var beginDateTime: String?,
    var endDateTime: String?,
    var timeLimit: String?,
    var passScore: Int?,
    var cancelled: Boolean?,
    var finished: Boolean?
): Serializable{

}