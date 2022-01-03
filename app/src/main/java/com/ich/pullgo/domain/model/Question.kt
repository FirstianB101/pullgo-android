<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/Question.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/Question.kt

import java.io.Serializable

class Question(
    var answer: List<Int>?,
    var choice: Map<String,String>?,
    var pictureUrl: String?,
    var content: String?,
    var examId: Long?
): Serializable {
    var id: Long? = null
}