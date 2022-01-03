<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/Academy.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.domain.model
>>>>>>> ich:app/src/main/java/com/ich/pullgo/domain/model/Academy.kt

import java.io.Serializable

class Academy(

    var name: String?,
    var phone: String?,
    var address: String?,
    var ownerId: Long?
) :Serializable {
    var id: Long? = null
    override fun toString(): String {
        return name ?: ""
    }
}