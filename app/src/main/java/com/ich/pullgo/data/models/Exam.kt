package com.ich.pullgo.data.models

import java.io.Serializable

class Exam (
    var classroomId: Long?,
    var creatorId: Long?,
    var name: String?,
    var beginDateTime: String?,
    var endDateTime: String?,
    var timeLimit: String?,
    var passScore: Int?,
    var cancelled: Boolean,
    var finished: Boolean
): Serializable{
    var id: Long? = null
}