<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/Lesson.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/Lesson.kt

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