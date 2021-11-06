package com.harry.pullgo.ui.commonFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harry.pullgo.data.api.OnCheckPwListener
import com.harry.pullgo.databinding.FragmentChangeInfoCheckPwBinding

class ChangeInfoCheckPwFragment: Fragment() {
    private val binding by lazy{FragmentChangeInfoCheckPwBinding.inflate(layoutInflater)}

    var pwCheckListener: OnCheckPwListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setListeners()

        return binding.root
    }

    private fun setListeners(){
        binding.buttonInfoCheckPw.setOnClickListener {
            pwCheckListener?.onPasswordCheck()
        }
    }
}