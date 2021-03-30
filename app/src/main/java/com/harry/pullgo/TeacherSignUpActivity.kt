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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView

import com.google.android.material.textfield.TextInputEditText;
import com.harry.pullgo.databinding.ActivitySignUpTeacherBinding
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.regex.Pattern;

class TeacherSignUpActivity:AppCompatActivity(), SignUpFragmentSwitch{
    private val binding by lazy{ActivitySignUpTeacherBinding.inflate(layoutInflater)}
    lateinit var signUpId:FragmentSignUpId
    lateinit var signUpPw:FragmentSignUpPw
    lateinit var signUpInfo:FragmentSignUpTeacherInfo

    var curPosition:Int=0
    private val signUpBundle = Bundle()

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
                val id=signUpBundle.getString("signUpId")
                val pw=signUpBundle.getString("signUpPw")
                val fullName=bundle?.getString("signUpTeacherFullName")
                val phone=bundle?.getString("signUpTeacherPhone")

                val teacher=Teacher(Account(id,fullName,phone))
                createTeacher(teacher)

                makePopup()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==1){
                val intent=Intent(applicationContext,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }

    private fun createTeacher(teacher:Teacher){
        val retrofit= RetrofitClient.getInstance()
        val service=retrofit.create(RetrofitService::class.java)

        service.createTeacher(teacher).enqueue(object: Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                if(response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
            }
        })
    }
    fun getTeachers():List<Teacher>?{
        val retrofit=RetrofitClient.getInstance()
        val service=retrofit.create(RetrofitService::class.java)
        var teacherList: List<Teacher>? = null
        service.getTeachersList().enqueue(object: Callback<List<Teacher>>{
            override fun onResponse(call: Call<List<Teacher>>, response: Response<List<Teacher>>) {
                if(response.isSuccessful){
                    teacherList=response.body()
                }
            }

            override fun onFailure(call: Call<List<Teacher>>, t: Throwable) {
                t.printStackTrace()
            }
        })
        if(teacherList!=null){
            for(i in teacherList!!){
                Log.d("teacher","teacher: $i")
            }
        }
        return teacherList
    }

    private fun makePopup(){
        val intent= Intent(baseContext, PopupActivity::class.java)
        intent.putExtra("type", PopupType.NORMAL)
        intent.putExtra("gravity", PopupGravity.CENTER)
        intent.putExtra("title", "회원가입 완료!")
        intent.putExtra("content", "가입하신 정보로 로그인해주세요")
        intent.putExtra("buttonCenter", "로그인 화면으로")
        startActivityForResult(intent,1)
    }
}

