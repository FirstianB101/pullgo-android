package com.harry.pullgo.application

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.harry.pullgo.data.models.User
import com.harry.pullgo.ui.commonFragment.LoadingDialogFragment
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@Singleton
@HiltAndroidApp
class PullgoApplication: Application() {
    init {
        instance = this
    }

    var loginUser = User()
    private val loadingDialog by lazy { LoadingDialogFragment() }

    fun showLoadingDialog(fragmentManager: FragmentManager){
        loadingDialog.show(fragmentManager,"Loading Dialog")
    }

    fun dismissLoadingDialog(){
        loadingDialog.dismiss()
    }

    companion object{
        var instance: PullgoApplication? = null
    }
}