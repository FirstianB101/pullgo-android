package com.harry.pullgo.application

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.harry.pullgo.data.models.User
import com.harry.pullgo.ui.commonFragment.LoadingDialogFragment
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@Singleton
@HiltAndroidApp
class PullgoApplication: Application() {
    var loginUser = User()
    private val loadingDialog by lazy { LoadingDialogFragment() }

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
}