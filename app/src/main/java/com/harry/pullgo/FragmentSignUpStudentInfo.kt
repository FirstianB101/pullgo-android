package com.harry.pullgo

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.harry.pullgo.databinding.FragmentSignupStudentInfoBinding

class FragmentSignUpStudentInfo: Fragment() {
    private val binding by lazy{ FragmentSignupStudentInfoBinding.inflate(layoutInflater) }
    private val PHONE_TYPE_EXPRESSION="^[0-9]{10,11}*$"

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
                    val bundle=Bundle()
                    callbackListener?.onDataPass(3,bundle)
                }
                .customView(R.layout.sign_up_dialog)
                .show()
        }

        return binding.root
    }

    private val phoneWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
}