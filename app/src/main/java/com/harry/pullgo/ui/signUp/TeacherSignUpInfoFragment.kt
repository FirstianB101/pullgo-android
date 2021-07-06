package com.harry.pullgo.ui.signUp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.databinding.FragmentSignupTeacherInfoBinding
import com.harry.pullgo.data.api.SignUpFragmentSwitch
import com.harry.pullgo.data.objects.Account
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import java.util.regex.Pattern

class TeacherSignUpInfoFragment: Fragment() {
    private val binding by lazy{FragmentSignupTeacherInfoBinding.inflate(layoutInflater)}
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

    private lateinit var viewModel: SignUpViewModel

    var callbackListener: SignUpFragmentSwitch?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackListener=context as SignUpFragmentSwitch
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel= ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        setListeners()

        return binding.root
    }

    private fun setListeners(){
        binding.signUpTeacherPhone.addTextChangedListener(phoneWatcher)

        binding.buttonSignUpTeacherPhoneVerifyRequest.setOnClickListener {

        }
        binding.buttonTeacherSignUpPhoneVerifyCheck.setOnClickListener {
            isCertificated=true
        }

        binding.buttonTeacherSignUpSuccess.setOnClickListener {
            if(checkEmptyExist()){
                enrollTeacher()
                callbackListener?.onDataPass(3)
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun enrollTeacher(){
        val teacherName = binding.signUpTeacherName.text.toString()
        val teacherPhone = binding.signUpTeacherPhone.text.toString()
        val userName = viewModel.signUpId.value

        val account = Account(userName,teacherName,teacherPhone)
        val teacher = Teacher(account)

        viewModel.signUpTeacher.postValue(teacher)
    }

    private fun checkEmptyExist():Boolean{
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