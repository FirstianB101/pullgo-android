package com.harry.pullgo
class Account {
    var userName:String
    var password:String
    var fullName:String
    var phone:String
    constructor(userName:String, password:String, fullName:String, phone:String) {
        this.userName = userName
        this.password = password
        this.fullName = fullName
        this.phone = phone
    }
    constructor(source:Account) {
        this.userName = source.userName
        this.password = source.password
        this.fullName = source.fullName
        this.phone = source.phone
    }
}