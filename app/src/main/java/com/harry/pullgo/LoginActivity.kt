package com.harry.pullgo;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.harry.pullgo.databinding.ActivityLoginBinding
import com.harry.pullgo.interfaces.RetrofitClient
import com.harry.pullgo.interfaces.RetrofitService
import com.harry.pullgo.objects.Student
import com.harry.pullgo.objects.Teacher
import com.harry.pullgo.studentActivity.StudentMainActivity
import com.harry.pullgo.teacherActivity.TeacherMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity: AppCompatActivity(){
    private val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}
    var loginedStudent: Student? = null
    var loginedTeacher: Teacher? = null

    var isStudent=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginSwitchButton.setOnSwitchListener{ position, _ ->
            isStudent = (position==0)
        }

        binding.buttonSignUp.setOnClickListener {
            val intent=Intent(applicationContext,SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        binding.buttonFindAccount.setOnClickListener {
            val intent=Intent(applicationContext,FindAccountActivity::class.java)
            intent.putExtra("isStudent",isStudent)

            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {

            if(isStudent){
                val intent1=Intent(applicationContext, StudentMainActivity::class.java)
                studentLogin(binding.loginId.text.toString().toLong(),intent1)
            }else{
                val intent2=Intent(applicationContext, TeacherMainActivity::class.java)
                teacherLogin(binding.loginId.text.toString().toLong(),intent2)
            }
        }
    }

    private fun teacherLogin(id:Long,intent:Intent){
        val retrofit= RetrofitClient.getInstance()
        val service=retrofit.create(RetrofitService::class.java)
        var teacher: Teacher? = null

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                teacher=service.getTeacher(id).execute().body()
            }.await()
            loginedTeacher=teacher
            intent.putExtra("fullName",loginedTeacher?.account?.fullName)
            intent.putExtra("userName",loginedTeacher?.account?.username)
            intent.putExtra("id",loginedTeacher?.id)
            loginedTeacher=null     //메인 진입 시 저장 안하게?
            startActivity(intent)
        }
    }

    private fun studentLogin(id:Long,intent:Intent){
        val retrofit= RetrofitClient.getInstance()
        val service=retrofit.create(RetrofitService::class.java)
        var student: Student? = null

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                student=service.getStudent(id).execute().body()
            }.await()
            loginedStudent=student
            intent.putExtra("fullName",loginedStudent?.account?.fullName)
            intent.putExtra("id",loginedStudent?.account?.username)
            loginedStudent=null
            startActivity(intent)
        }
    }
}