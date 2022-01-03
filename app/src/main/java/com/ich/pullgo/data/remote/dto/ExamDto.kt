package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Exam
import java.io.Serializable

class ExamDto (
    var id: Long? = null,
    var classroomId: Long?,
    var creatorId: Long?,
    var name: String?,
    var beginDateTime: String?,
    var endDateTime: String?,
    var timeLimit: String?,
    var passScore: Int?,
    var cancelled: Boolean,
    var finished: Boolean
): Serializable

fun ExamDto.toExam(): Exam {
    val exam = Exam(classroomId, creatorId, name, beginDateTime, endDateTime, timeLimit, passScore, cancelled, finished)
    exam.id = id
    return exam
}