package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Classroom
import java.io.Serializable

class ClassroomDto(
    var id: Long? = null,
    var creator: TeacherDto? = null,
    var academyId: Long?,
    var name: String?,
    var creatorId: Long?
):Serializable

fun ClassroomDto.toClassroom(): Classroom{
    val classroom = Classroom(academyId, name, creatorId)
    classroom.id = id
    classroom.creator = creator?.toTeacher()
    return classroom
}