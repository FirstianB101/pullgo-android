package com.ich.pullgo.ui.signUp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.ich.pullgo.databinding.FragmentSignupTeacherInfoBinding
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/signUp/TeacherSignUpInfoFragment.kt
import com.ich.pullgo.data.models.Account
import com.ich.pullgo.data.models.Teacher
=======
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Teacher
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/signUp/TeacherSignUpInfoFragment.kt
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class TeacherSignUpInfoFragment: Fragment() {
    private val binding by lazy{FragmentSignupTeacherInfoBinding.inflate(layoutInflater)}
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

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
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun enrollTeacher(){
        val teacherName = binding.signUpTeacherName.text.toString()
        val teacherPhone = binding.signUpTeacherPhone.text.toString()
        val userName = viewModel.signUpId.value
        val password = viewModel.signUpPw.value

        val account = Account(userName,teacherName,teacherPhone,password)
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
            binding.signUpTeacherPhoneInputLayout.error=null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.signUpTeacherPhoneInputLayout.error="숫자만 입력해 주세요"
            }
        }

    }
}