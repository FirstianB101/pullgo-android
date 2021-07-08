package com.harry.pullgo.data.objects

import java.io.Serializable

class Academy(
    var id: Long?,
    var name: String?,
    var phone: String?,
    var address: String?,
    var ownerId: Long?
) :Serializable {

}