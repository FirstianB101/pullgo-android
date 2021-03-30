package com.harry.pullgo;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.gson.Gson
import com.harry.pullgo.databinding.ActivitySignUpStudentBinding
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentSignUpActivity:AppCompatActivity(),SignUpFragmentSwitch{
    private val binding by lazy{ActivitySignUpStudentBinding.inflate(layoutInflater)}
    lateinit var signUpId:FragmentSignUpId
    lateinit var signUpPw:FragmentSignUpPw
    lateinit var signUpInfo:FragmentSignUpStudentInfo

    var curPosition:Int=0
    private val signUpBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        signUpInfo = FragmentSignUpStudentInfo()
        signUpPw = FragmentSignUpPw()
        signUpId = FragmentSignUpId()
        supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpId).commit()
    }

    override fun onBackPressed() {
        if(curPosition==0){
            super.onBackPressed()
        }else{
            onDataPass(curPosition-1,null)
        }
    }

    override fun onDataPass(position: Int,bundle:Bundle?) {
        when(position){
            0->{//아이디 입력 프래그먼트
                supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpId).commit()
                curPosition=0
            }
            1->{//아이디 입력 프래그먼트에서 패스워드 입력으로 넘겨달라 요청
                signUpBundle.putString("signUpId",bundle?.getString("signUpId"))
                supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpPw).commit()
                curPosition=1
            }
            2->{//패스워드 입력 프래그먼트에서 정보 입력으로 넘겨달라 요청
                signUpBundle.putString("signUpPw",bundle?.getString("signUpPw"))
                supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpInfo).commit()
                curPosition=2
            }
            3->{//정보 입력 프래그먼트에서 마무리
                val id=signUpBundle.getString("signUpId")
                val pw=signUpBundle.getString("signUpPw")
                val fullName=bundle?.getString("signUpStudentFullName")
                val phone=bundle?.getString("signUpStudentPhone")
                val parentPhone=bundle?.getString("signUpParentPhone")
                val schoolName=bundle?.getString("signUpStudentSchoolName")
                val schoolYear=bundle?.getInt("signUpSchoolYear")

                val account=Account(id,fullName,phone)
                val student=Student(account,parentPhone,schoolName,schoolYear)
                createStudent(student)

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

    private fun createStudent(student:Student){
        val retrofit= RetrofitClient.getInstance()
        val service=retrofit.create(RetrofitService::class.java)

        service.createStudent(student).enqueue(object: Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if(response.isSuccessful){
                    val stu:Student?=response.body()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
            }
        })
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