package com.harry.pullgo.application

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.harry.pullgo.data.models.User
import com.harry.pullgo.ui.commonFragment.LoadingDialogFragment

class PullgoApplication: Application() {
    var loginUser = User()
    private val loadingDialog by lazy { LoadingDialogFragment() }

    fun showLoadingDialog(fragmentManager: FragmentManager){
        loadingDialog.show(fragmentManager,"Loading Dialog")
    }

    fun dismissLoadingDialog(){
        loadingDialog.dismiss()
    }
}