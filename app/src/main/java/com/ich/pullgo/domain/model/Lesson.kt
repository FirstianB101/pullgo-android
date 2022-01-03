package com.ich.pullgo.domain.model

import com.ich.pullgo.data.remote.dto.Schedule
import java.io.Serializable

class Lesson(
    var name: String?,
    var classroomId: Long?,
    var schedule: Schedule?,
    var academyId: Long?
):Serializable {
    var id: Long? = null
}