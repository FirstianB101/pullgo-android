package com.ich.pullgo.domain.model

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

fun Exam.copy(src: Exam){
    this.classroomId = src.classroomId
    this.creatorId = src.creatorId
    this.name = src.name
    this.beginDateTime = src.beginDateTime
    this.endDateTime = src.endDateTime
    this.timeLimit = src.timeLimit
    this.passScore = src.passScore
    this.cancelled = src.cancelled
    this.finished = src.finished
    this.id = src.id
}