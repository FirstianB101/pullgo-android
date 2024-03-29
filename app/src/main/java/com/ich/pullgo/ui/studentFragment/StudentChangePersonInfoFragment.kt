package com.ich.pullgo.ui.studentFragment

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
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.databinding.FragmentStudentChangeInfoBinding
import com.ich.pullgo.ui.commonFragment.ChangeInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class StudentChangePersonInfoFragment : Fragment() {
    val binding by lazy { FragmentStudentChangeInfoBinding.inflate(layoutInflater) }
    private val PHONE_TYPE_EXPRESSION="^[0-9]*$"
    private var isCertificated=false

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
        binding.changeStudentName.setText(app.loginUser.student?.account?.fullName)
        binding.changeStudentPhone.setText(app.loginUser.student?.account?.phone)
        binding.changeParentPhone.setText(app.loginUser.student?.parentPhone)
        binding.changeSchoolName.setText(app.loginUser.student?.schoolName)
        binding.changeGradeSwitchButton.selectedTab = app.loginUser.student?.schoolYear!! - 1
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
        val userName = app.loginUser.student?.account?.username
        val password = app.loginUser.student?.account?.password

        val account = Account(userName,studentName,studentPhone,password)
        val student = Student(account,studentParentPhone,studentSchoolName,studentSchoolYear)
        student.id = app.loginUser.student?.id

        viewModel.changeStudentInfo(student.id!!,student)
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