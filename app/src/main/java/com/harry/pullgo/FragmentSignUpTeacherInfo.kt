package com.harry.pullgo

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.databinding.FragmentSignupTeacherInfoBinding
import java.util.regex.Pattern

class FragmentSignUpTeacherInfo: Fragment() {
    private val binding by lazy{FragmentSignupTeacherInfoBinding.inflate(layoutInflater)}
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
        binding.signUpTeacherPhone.addTextChangedListener(phoneWatcher)

        binding.buttonSignUpTeacherPhoneVerifyRequest.setOnClickListener {

        }
        binding.buttonTeacherSignUpPhoneVerifyCheck.setOnClickListener {
            isCertificated=true
        }

        binding.buttonTeacherSignUpSuccess.setOnClickListener {
            if(checkEmptyExist()){
                val bundle=Bundle()
                bundle.putString("signUpTeacherFullName",binding.signUpTeacherName.text.toString())
                bundle.putString("signUpTeacherPhone",binding.signUpTeacherPhone.text.toString())
                callbackListener?.onDataPass(3,bundle)
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요",Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun checkEmptyExist():Boolean{
        return (binding.signUpTeacherName.text.toString()!="")&&
                (binding.signUpTeacherPhone.text.toString()!="")&&
                (isCertificated)
    }

    private val phoneWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.signUpTeacherPhoneLayout.error=null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.signUpTeacherPhoneLayout.error="숫자만 입력해 주세요"
            }
        }

    }
}