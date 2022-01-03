package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Lesson
import java.io.Serializable

class LessonDto(
    var id: Long? = null,
    var name: String?,
    var classroomId: Long?,
    var schedule: Schedule?,
    var academyId: Long?
):Serializable

fun LessonDto.toLesson(): Lesson{
    val lesson = Lesson(name, classroomId, schedule, academyId)
    lesson.id = id
    return lesson
}

class Schedule(
    var date: String?,
    var beginTime: String?,
    var endTime: String?
):Serializable