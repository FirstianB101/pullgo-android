package com.harry.pullgo.objects

import java.io.Serializable

class Student : Serializable{
    var id:Long?=null
    var account: Account?=null
    var parentPhone:String?=null
    var schoolName:String?=null
    var schoolYear:Int?=null

    constructor(account: Account?, parentPhone:String?, schoolName:String?, schoolYear:Int?){
        this.account=account
        this.parentPhone=parentPhone
        this.schoolName=schoolName
        this.schoolYear=schoolYear
    }
    override fun toString(): String {
        return "account:$account, parentPhone:$parentPhone, schoolName:$schoolName, schoolYear:$schoolYear"
    }
}