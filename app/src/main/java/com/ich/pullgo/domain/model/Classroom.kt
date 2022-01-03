<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/Classroom.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/Classroom.kt

import java.io.Serializable

class Classroom(
    var academyId: Long?,
    var name: String?,
    var creatorId: Long?
):Serializable {
    var id: Long? = null
    var creator: Teacher? = null
    override fun toString(): String {
        return name?.split(';')?.get(0) ?: ""
    }
}