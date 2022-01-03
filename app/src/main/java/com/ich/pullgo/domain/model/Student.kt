<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/Student.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/Student.kt

import java.io.Serializable

class Student(
    var account: Account?,
    var parentPhone: String?,
    var schoolName: String?,
    var schoolYear: Int?
) : Serializable{
    var id:Long?=null

    override fun toString(): String {
        return "${account?.fullName} (${account?.username})"
    }
}