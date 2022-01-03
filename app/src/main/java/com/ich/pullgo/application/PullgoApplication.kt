package com.ich.pullgo.application

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.ui.commonFragment.LoadingDialogFragment
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@Singleton
@HiltAndroidApp
class PullgoApplication: Application() {
    private val loadingDialog by lazy { LoadingDialogFragment() }
    var loginUser: User = User()

    init {
        instance = this
    }

    fun showLoadingDialog(fragmentManager: FragmentManager){
        if(loadingDialog.isAdded) {
            loadingDialog.dismiss()
        }
        loadingDialog.show(fragmentManager, "Loading Dialog")
    }

    fun dismissLoadingDialog(){
        loadingDialog.dismiss()
    }

    companion object{
        var instance: PullgoApplication? = null
    }


    fun loginUser(user: User){
        loginUser = user
    }

    fun logoutUser(){
        loginUser = User()
    }
}