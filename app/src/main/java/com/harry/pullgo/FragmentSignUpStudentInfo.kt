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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.databinding.FragmentSignupStudentInfoBinding
import java.util.regex.Pattern

class FragmentSignUpStudentInfo: Fragment() {
    private val binding by lazy{ FragmentSignupStudentInfoBinding.inflate(layoutInflater) }
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

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
        binding.signUpStudentPhone.addTextChangedListener(phoneWatcher)
        binding.signUpParentPhone.addTextChangedListener(parentPhoneWatcher)

        binding.buttonSignUpStudentPhoneVerifyRequest.setOnClickListener{

        }

        binding.buttonStudentSignUpPhoneVerifyCheck.setOnClickListener {
            isCertificated=true
        }

        binding.buttonStudentSignUpSuccess.setOnClickListener {
            if(checkEmptyExist()){
                val bundle=Bundle()
                bundle.putString("signUpStudentFullName",binding.signUpStudentName.text.toString())
                bundle.putString("signUpStudentPhone",binding.signUpStudentPhone.text.toString())
                bundle.putString("signUpParentPhone",binding.signUpParentPhone.text.toString())
                bundle.putString("signUpStudentSchoolName",binding.signUpSchoolName.text.toString())
                bundle.putInt("signUpSchoolYear",binding.signUpGradeSwitchButton.selectedTab+1)
                callbackListener?.onDataPass(3,bundle)
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun checkEmptyExist():Boolean{
        return (binding.signUpStudentName.text.toString()!="")&&
                (binding.signUpStudentPhone.text.toString()!="")&&
                (binding.signUpParentPhone.text.toString()!="")&&
                (binding.signUpSchoolName.text.toString()!="")&&
                (isCertificated)
    }

    private val phoneWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.signUpPhoneLayout.error=null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.signUpPhoneLayout.error="숫자만 입력해 주세요"
            }
        }

    }
    private val parentPhoneWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.signUpParentPhoneLayout.error=null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.signUpParentPhoneLayout.error="숫자만 입력해 주세요"
            }
        }

    }
}