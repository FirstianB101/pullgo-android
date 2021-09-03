package com.harry.pullgo.ui.login;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.databinding.ActivityLoginBinding
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.LoginRepository
import com.harry.pullgo.ui.findAccount.FindAccountActivity
import com.harry.pullgo.ui.signUp.SignUpActivity
import com.harry.pullgo.ui.main.StudentMainActivity
import com.harry.pullgo.ui.main.StudentMainActivityNoAcademy
import com.harry.pullgo.ui.main.TeacherMainActivity
import com.harry.pullgo.ui.main.TeacherMainActivityNoAcademy

class LoginActivity: AppCompatActivity(){
    private val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}

    private val viewModel: LoginViewModel by viewModels{LoginViewModelFactory(LoginRepository())}

    var isStudent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        autoLogin()
        setClickListeners()
        initViewModel()
    }

    private fun autoLogin(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val loginId = pref.getString("loginId",null)
        val loginPw = pref.getString("loginPw",null)
        val wasStudent = pref.getBoolean("wasStudent",false)
        val autoLoginChecked = pref.getBoolean("autoLoginChecked",false)

        if(loginId != null && loginPw != null){
            binding.loginId.setText(loginId)
            binding.loginPw.setText(loginPw)
            binding.loginSwitchButton.selectedTab = if(wasStudent) 0 else 1
            binding.checkBoxAutoLogin.isChecked = autoLoginChecked

            if(wasStudent){
                viewModel.requestStudentLogin(loginId.toLong())
            }else{
                viewModel.requestTeacherLogin(loginId.toLong())
            }
        }
    }

    private fun initViewModel(){
        viewModel.loginStudentRepositories.observe(this){
            viewModel.requestStudentAcademies(binding.loginId.text.toString().toLong())
        }

        viewModel.academyRepositoryStudentApplied.observe(this){
            val studentAcademies = viewModel.academyRepositoryStudentApplied.value

            if(binding.checkBoxAutoLogin.isChecked){
                saveAutoLoginInfo()
            }else{
                resetAutoLoginInfo()
            }

            val mainIntent = if(studentAcademies.isNullOrEmpty())
                Intent(applicationContext, StudentMainActivityNoAcademy::class.java)
            else
                Intent(applicationContext, StudentMainActivity::class.java)

            startMainStudent(mainIntent)
        }

        viewModel.loginTeacherRepositories.observe(this){
            viewModel.requestTeacherAcademies(binding.loginId.text.toString().toLong())
        }

        viewModel.academyRepositoryTeacherApplied.observe(this){
            val teacherAcademies = viewModel.academyRepositoryTeacherApplied.value

            if(binding.checkBoxAutoLogin.isChecked){
                saveAutoLoginInfo()
            }else{
                resetAutoLoginInfo()
            }

            val mainIntent = if(teacherAcademies.isNullOrEmpty())
                Intent(applicationContext, TeacherMainActivityNoAcademy::class.java)
            else
                Intent(applicationContext, TeacherMainActivity::class.java)

            startMainTeacher(mainIntent)
        }
    }

    private fun saveAutoLoginInfo(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val autoLogin = pref.edit()

        autoLogin.putString("loginId",binding.loginId.text.toString())
        autoLogin.putString("loginPw",binding.loginPw.text.toString())
        autoLogin.putBoolean("wasStudent",isStudent)
        autoLogin.putBoolean("autoLoginChecked",binding.checkBoxAutoLogin.isChecked)
        autoLogin.apply()
    }

    private fun resetAutoLoginInfo(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val autoLogin = pref.edit()

        autoLogin.putString("loginId",null)
        autoLogin.putString("loginPw",null)
        autoLogin.putBoolean("wasStudent",true)
        autoLogin.putBoolean("autoLoginChecked",false)
        autoLogin.apply()
    }

    private fun setClickListeners(){
        binding.loginSwitchButton.setOnSwitchListener{ position, _ ->
            isStudent = (position==0)
        }

        binding.buttonSignUp.setOnClickListener {
            val intent=Intent(applicationContext, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        binding.buttonFindAccount.setOnClickListener {
            val intent=Intent(applicationContext, FindAccountActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            if(isStudent)
                viewModel.requestStudentLogin(binding.loginId.text.toString().toLong())
            else
                viewModel.requestTeacherLogin(binding.loginId.text.toString().toLong())
        }
    }

    private fun startMainStudent(intent: Intent){
        val student = viewModel.loginStudentRepositories.value
        if(student?.id != null) {
            LoginInfo.loginStudent = student
            LoginInfo.loginTeacher = null
            startActivity(intent)
        }
    }

    private fun startMainTeacher(intent: Intent){
        val teacher = viewModel.loginTeacherRepositories.value
        if(teacher?.id != null) {
            LoginInfo.loginStudent = null
            LoginInfo.loginTeacher = teacher
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PREVIOUS_ID,binding.loginId.text.toString())
        outState.putString(PREVIOUS_PW,binding.loginPw.text.toString())
        outState.putBoolean(PREVIOUS_IS_STUDENT,isStudent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.loginId.setText(savedInstanceState.getString(PREVIOUS_ID))
        binding.loginPw.setText(savedInstanceState.getString(PREVIOUS_PW))
        binding.loginSwitchButton.selectedTab = if(savedInstanceState.getBoolean(PREVIOUS_IS_STUDENT)) 0 else 1
    }

    private val PREVIOUS_ID = "previous_id"
    private val PREVIOUS_PW = "previous_pw"
    private val PREVIOUS_IS_STUDENT = "previous_is_student"
}