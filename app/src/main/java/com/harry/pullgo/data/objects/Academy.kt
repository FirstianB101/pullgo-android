package com.harry.pullgo.data.objects

import java.io.Serializable

class Academy:Serializable {
    var id:Long?=null
    var name:String?=null
    var phone:String?=null
    var address:String?=null
    var ownerId:Long?=null

    constructor(id:Long?,name:String?,phone:String?,address:String?,ownerId:Long?){
        this.id=id
        this.name=name
        this.phone=phone
        this.address=address
        this.ownerId=ownerId
    }
}