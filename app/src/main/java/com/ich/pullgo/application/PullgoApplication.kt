package com.ich.pullgo.application

import android.app.Application
import com.ich.pullgo.domain.model.User
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PullgoApplication: Application(){
    private var loginUser: User? = null
    var academyExist = false

    init {
        instance = this
    }

    fun loginUser(user: User?){
        loginUser = user
    }

    fun logoutUser(){
        loginUser = null
    }

    fun getLoginUser() = loginUser

    companion object{
        var instance: PullgoApplication? = null
    }
}