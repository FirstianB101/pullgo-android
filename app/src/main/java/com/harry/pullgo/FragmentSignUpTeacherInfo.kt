package com.harry.pullgo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.harry.pullgo.databinding.FragmentSignupTeacherInfoBinding

class FragmentSignUpTeacherInfo: Fragment() {
    private val binding by lazy{FragmentSignupTeacherInfoBinding.inflate(layoutInflater)}

    var callbackListener: SignUpFragmentSwitch?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackListener=context as SignUpFragmentSwitch
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.buttonSignUpSuccess.setOnClickListener {
            MaterialDialog(requireContext())
                .positiveButton(null,"완료")
                .positiveButton {
                    val bundle= Bundle()
                    callbackListener?.onDataPass(3,bundle)
                }
                .customView(R.layout.sign_up_dialog)
                .show()
        }

        return binding.root
    }
}