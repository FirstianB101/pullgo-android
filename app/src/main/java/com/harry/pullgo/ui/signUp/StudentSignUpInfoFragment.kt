package com.harry.pullgo.ui.signUp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.databinding.FragmentSignupStudentInfoBinding
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.models.Student
import java.util.regex.Pattern

class StudentSignUpInfoFragment: Fragment() {
    private val binding by lazy{ FragmentSignupStudentInfoBinding.inflate(layoutInflater) }
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        setListeners()

        return binding.root
    }

    private fun setListeners(){
        binding.signUpStudentPhone.addTextChangedListener(phoneWatcher)
        binding.signUpParentPhone.addTextChangedListener(parentPhoneWatcher)

        binding.buttonSignUpStudentPhoneVerifyRequest.setOnClickListener{

        }

        binding.buttonStudentSignUpPhoneVerifyCheck.setOnClickListener {
            isCertificated=true
        }

        binding.buttonStudentSignUpSuccess.setOnClickListener {
            if(checkEmptyExist()){
                enrollStudent()
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun enrollStudent(){
        val studentName = binding.signUpStudentName.text.toString()
        val studentPhone = binding.signUpStudentPhone.text.toString()
        val studentParentPhone = binding.signUpParentPhone.text.toString()
        val studentSchoolName = binding.signUpSchoolName.text.toString()
        val studentSchoolYear = binding.signUpGradeSwitchButton.selectedTab+1
        val userName = viewModel.signUpId.value

        val account = Account(userName,studentName,studentPhone)
        val student = Student(account,studentParentPhone,studentSchoolName,studentSchoolYear)

        viewModel.signUpStudent.postValue(student)
    }

    private fun checkEmptyExist():Boolean{
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