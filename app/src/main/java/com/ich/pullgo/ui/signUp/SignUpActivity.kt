package com.ich.pullgo.ui.signUp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ich.pullgo.R
import com.ich.pullgo.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity: AppCompatActivity(), View.OnClickListener{
    private val binding by lazy{ActivitySignUpBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setClickListeners()
    }

    private fun setClickListeners(){
        binding.buttonSignUpAsStudent.setOnClickListener(this)
        binding.buttonSignUpAsTeacher.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonSignUpAsStudent ->{
               val intent= Intent(applicationContext, StudentSignUpActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
               startActivity(intent)
            }
            R.id.buttonSignUpAsTeacher ->{
                val intent= Intent(applicationContext, TeacherSignUpActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}