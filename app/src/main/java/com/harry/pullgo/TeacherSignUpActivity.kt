package com.harry.pullgo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.harry.pullgo.databinding.ActivitySignUpTeacherBinding

import java.util.regex.Pattern;

class TeacherSignUpActivity:AppCompatActivity(), SignUpFragmentSwitch{
    private val binding by lazy{ActivitySignUpTeacherBinding.inflate(layoutInflater)}
    lateinit var signUpId:FragmentSignUpId
    lateinit var signUpPw:FragmentSignUpPw
    lateinit var signUpInfo:FragmentSignUpTeacherInfo

    var curPosition:Int=0
    val signUpBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        signUpInfo = FragmentSignUpTeacherInfo()
        signUpPw = FragmentSignUpPw()
        signUpId = FragmentSignUpId()
        supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpId).commit()
    }

    override fun onBackPressed() {
        if(curPosition==0){
            super.onBackPressed()
        }else{
            onDataPass(curPosition-1,null)
        }
    }

    override fun onDataPass(position: Int, bundle: Bundle?) {
        when(position){
            0->{//아이디 입력 프래그먼트
                supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpId).commit()
                curPosition=0
            }
            1->{//아이디 입력 프래그먼트에서 패스워드 입력으로 넘겨달라 요청
                signUpBundle.putString("signUpId",bundle?.getString("signUpId"))
                supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpPw).commit()
                curPosition=1
            }
            2->{//패스워드 입력 프래그먼트에서 정보 입력으로 넘겨달라 요청
                signUpBundle.putString("signUpPw",bundle?.getString("signUpPw"))
                supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpInfo).commit()
                curPosition=2
            }
            3->{//정보 입력 프래그먼트에서 마무리
                Log.d("signupTeacher","id:${signUpBundle.getString("signUpId")}")
                Log.d("signupTeacher","pw:${signUpBundle.getString("signUpPw")}")
                val intent = Intent(this,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }

}

