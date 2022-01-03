<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/AttenderState.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/AttenderState.kt

import java.io.Serializable

class AttenderState(
    var attenderId: Long?,
    var examId: Long?,
    var progress: String?,
    var score: Int?
):Serializable{
    var id: Long? = null
}