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
    private var toastList: MutableList<Toast>
    private val loadingDialog by lazy { LoadingDialogFragment() }

    init {
        instance = this
        toastList = mutableListOf()
    }

    fun showLoadingDialog(fragmentManager: FragmentManager){
        loadingDialog.show(fragmentManager,"Loading Dialog")
    }

    fun dismissLoadingDialog(){
        loadingDialog.dismiss()
    }

    fun showToast(msg: String){
        Handler(Looper.getMainLooper()).post {
            val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            toastList.add(toast)
            toast.show()
        }
    }

    fun cancelAllToasts(){
        for(toast in toastList){
            toast.cancel()
        }
        toastList.clear()
    }

    companion object{
        var instance: PullgoApplication? = null
    }
}