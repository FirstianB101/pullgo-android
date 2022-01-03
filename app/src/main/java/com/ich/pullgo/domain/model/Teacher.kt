<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/Teacher.kt
package com.ich.pullgo.data.models;
=======
package com.ich.pullgo.domain.model;
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/Teacher.kt

import java.io.Serializable

class Teacher(var account: Account?) : Serializable{
    var id:Long? = null

    override fun toString(): String {
        return "${account?.fullName} (${account?.username})"
    }
}