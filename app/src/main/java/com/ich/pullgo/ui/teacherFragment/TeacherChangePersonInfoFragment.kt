package com.ich.pullgo.ui.teacherFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.models.Account
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.databinding.FragmentTeacherChangeInfoBinding
import com.ich.pullgo.ui.commonFragment.ChangeInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class TeacherChangePersonInfoFragment : Fragment() {
    private val binding by lazy{ FragmentTeacherChangeInfoBinding.inflate(layoutInflater)}
    private val PHONE_TYPE_EXPRESSION = "^[0-9]*$"
    private var isCertificated = false

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ChangeInfoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setListeners()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initialize()
    }

    private fun initialize(){
        binding.changeTeacherName.setText(app.loginUser.teacher?.account?.fullName)
        binding.changeTeacherPhone.setText(app.loginUser.teacher?.account?.phone)
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
        val userName = app.loginUser.teacher?.account?.username
        val password = app.loginUser.teacher?.account?.password

        val account = Account(userName,teacherName,teacherPhone,password)
        val teacher = Teacher(account)
        teacher.id = app.loginUser.teacher?.id

        viewModel.changeTeacherInfo(teacher.id!!,teacher)
    }

    private fun checkEmptyExist():Boolean{
        return (binding.changeTeacherName.text.toString() != "")&&
                (binding.changeTeacherPhone.text.toString() != "")&&
                (isCertificated)
    }

    private val phoneWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.changeTeacherPhoneInputLayout.error = null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.changeTeacherPhoneInputLayout.error = "숫자만 입력해 주세요"
            }
        }

    }
}