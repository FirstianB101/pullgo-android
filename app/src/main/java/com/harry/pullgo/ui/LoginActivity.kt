package com.harry.pullgo.ui;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.databinding.ActivityLoginBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.LoginRepository
import com.harry.pullgo.ui.studentActivity.StudentMainActivity
import com.harry.pullgo.ui.teacherActivity.TeacherMainActivity
import kotlinx.coroutines.*

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
        viewModel=ViewModelProvider(this,viewModelFactory).get(LoginViewModel::class.java)

        viewModel.loginStudentRepositories.observe(this){
            val mainIntent=Intent(applicationContext, StudentMainActivity::class.java)
            startMainStudent(mainIntent)
        }

        viewModel.loginTeacherRepositories.observe(this){
            val mainIntent=Intent(applicationContext, TeacherMainActivity::class.java)
            startMainTeacher(mainIntent)
        }
    }

    private fun getStudent(){
        viewModel.requestStudentLogin(binding.loginId.text.toString().toLong())
    }

    private fun getTeacher(){
        viewModel.requestTeacherLogin(binding.loginId.text.toString().toLong())
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
            if(isStudent){
                getStudent()
            }else{
                getTeacher()
            }
        }
    }

    private fun startMainStudent(intent: Intent){
        val student = viewModel.loginStudentRepositories.value
        intent.putExtra("fullName",student?.account?.fullName)
        intent.putExtra("userName",student?.account?.username)
        intent.putExtra("id",student?.id)
        LoginInfo.loginStudent=student
        startActivity(intent)
    }

    private fun startMainTeacher(intent: Intent){
        val teacher = viewModel.loginTeacherRepositories.value
        intent.putExtra("fullName",teacher?.account?.fullName)
        intent.putExtra("userName",teacher?.account?.username)
        intent.putExtra("id",teacher?.id)
        LoginInfo.loginTeacher=teacher
        startActivity(intent)
    }
}