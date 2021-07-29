package com.harry.pullgo.ui.teacherFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.objects.Account
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.databinding.FragmentTeacherChangeInfoBinding
import com.harry.pullgo.ui.main.ChangeInfoViewModel
import java.util.regex.Pattern

class TeacherChangePersonInfoFragment : Fragment() {
    private val binding by lazy{ FragmentTeacherChangeInfoBinding.inflate(layoutInflater)}
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

    private lateinit var viewModel: ChangeInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel= ViewModelProvider(requireActivity()).get(ChangeInfoViewModel::class.java)

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        binding.changeTeacherName.setText(LoginInfo.loginTeacher?.account?.fullName)
        binding.changeTeacherPhone.setText(LoginInfo.loginTeacher?.account?.phone)
    }

    private fun setListeners(){
        binding.changeTeacherPhone.addTextChangedListener(phoneWatcher)

        binding.buttonChangeTeacherPhoneVerifyRequest.setOnClickListener {

        }
        binding.buttonTeacherChangePhoneVerifyCheck.setOnClickListener {
            isCertificated=true
        }

        binding.buttonTeacherChangeSuccess.setOnClickListener {
            if(checkEmptyExist()){
                changeTeacherInfo()
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun changeTeacherInfo(){
        val teacherName = binding.changeTeacherName.text.toString()
        val teacherPhone = binding.changeTeacherPhone.text.toString()
        val userName = LoginInfo.loginTeacher?.account?.username

        val account = Account(userName,teacherName,teacherPhone)
        val teacher = Teacher(account)
        teacher.id=LoginInfo.loginTeacher?.id

        viewModel.changeTeacher.postValue(teacher)
    }

    private fun checkEmptyExist():Boolean{
        return (binding.changeTeacherName.text.toString()!="")&&
                (binding.changeTeacherPhone.text.toString()!="")&&
                (isCertificated)
    }

    private val phoneWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.changeTeacherPhoneLayout.error=null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.changeTeacherPhoneLayout.error="숫자만 입력해 주세요"
            }
        }

    }
}