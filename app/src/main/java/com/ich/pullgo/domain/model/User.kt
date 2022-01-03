<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/User.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/User.kt

import java.io.Serializable

class User : Serializable{
    var student: Student? = null
    var teacher: Teacher? = null
    var token: String? = null
}