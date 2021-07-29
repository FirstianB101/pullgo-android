package com.harry.pullgo.ui.login;

import android.content.Intent
import android.os.Bundle
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
    private lateinit var viewModel: LoginViewModel

    var isStudent=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setClickListeners()
        initViewModel()
    }

    private fun initViewModel(){
        val viewModelFactory = LoginViewModelFactory(LoginRepository())
        viewModel = ViewModelProvider(this,viewModelFactory).get(LoginViewModel::class.java)

        viewModel.loginStudentRepositories.observe(this){
            viewModel.requestStudentAcademies(binding.loginId.text.toString().toLong())
        }

        viewModel.academyRepositoryStudentApplied.observe(this){
            val studentAcademies = viewModel.academyRepositoryStudentApplied.value

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

            val mainIntent = if(teacherAcademies.isNullOrEmpty())
                Intent(applicationContext, TeacherMainActivityNoAcademy::class.java)
            else
                Intent(applicationContext, TeacherMainActivity::class.java)

            startMainTeacher(mainIntent)
        }
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
}