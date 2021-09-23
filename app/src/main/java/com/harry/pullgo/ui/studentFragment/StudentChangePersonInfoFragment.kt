package com.harry.pullgo.ui.studentFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.databinding.FragmentStudentChangeInfoBinding
import com.harry.pullgo.ui.main.ChangeInfoViewModel
import java.util.regex.Pattern

class StudentChangePersonInfoFragment : Fragment() {
    val binding by lazy { FragmentStudentChangeInfoBinding.inflate(layoutInflater) }
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

    private val viewModel: ChangeInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setListeners()

        return binding.root
    }

    private fun initialize(){
        binding.changeStudentName.setText(LoginInfo.loginStudent?.account?.fullName)
        binding.changeStudentPhone.setText(LoginInfo.loginStudent?.account?.phone)
        binding.changeParentPhone.setText(LoginInfo.loginStudent?.parentPhone)
        binding.changeSchoolName.setText(LoginInfo.loginStudent?.schoolName)
        binding.changeGradeSwitchButton.selectedTab = LoginInfo.loginStudent?.schoolYear!! - 1
    }

    private fun setListeners(){
        binding.changeStudentPhone.addTextChangedListener(phoneWatcher)
        binding.changeParentPhone.addTextChangedListener(parentPhoneWatcher)

        binding.buttonChangeStudentPhoneVerifyRequest.setOnClickListener{

        }

        binding.buttonStudentChangePhoneVerifyCheck.setOnClickListener {
            isCertificated=true
        }

        binding.buttonStudentChangeSuccess.setOnClickListener {
            if(checkEmptyExist()){
                changeStudentInfo()
            }
            else Snackbar.make(binding.root,"정보를 모두 입력해주세요", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun changeStudentInfo(){
        val studentName = binding.changeStudentName.text.toString()
        val studentPhone = binding.changeStudentPhone.text.toString()
        val studentParentPhone = binding.changeParentPhone.text.toString()
        val studentSchoolName = binding.changeSchoolName.text.toString()
        val studentSchoolYear = binding.changeGradeSwitchButton.selectedTab+1
        val userName = LoginInfo.loginStudent?.account?.username

        val account = Account(userName,studentName,studentPhone)
        val student = Student(account,studentParentPhone,studentSchoolName,studentSchoolYear)
        student.id = LoginInfo.loginStudent?.id

        viewModel.changeStudent.postValue(student)
    }

    private fun checkEmptyExist():Boolean{
        return (binding.changeStudentName.text.toString()!="")&&
                (binding.changeStudentPhone.text.toString()!="")&&
                (binding.changeParentPhone.text.toString()!="")&&
                (binding.changeSchoolName.text.toString()!="")&&
                (isCertificated)
    }

    private val phoneWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.changePhoneTextLayout.error = null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.changePhoneTextLayout.error = "숫자만 입력해 주세요"
            }
        }

    }
    private val parentPhoneWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.changeParentPhoneLayout.error=null
            if(!Pattern.matches(PHONE_TYPE_EXPRESSION,s.toString())){
                binding.changeParentPhoneLayout.error="숫자만 입력해 주세요"
            }
        }

    }
}