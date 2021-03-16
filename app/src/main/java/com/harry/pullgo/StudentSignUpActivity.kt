package com.harry.pullgo;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.harry.pullgo.databinding.ActivitySignUpStudentBinding

class StudentSignUpActivity:AppCompatActivity(),SignUpFragmentSwitch{
    private val binding by lazy{ActivitySignUpStudentBinding.inflate(layoutInflater)}
    lateinit var signUpId:FragmentSignUpId
    lateinit var signUpPw:FragmentSignUpPw
    lateinit var signUpInfo:FragmentSignUpStudentInfo

    var curPosition:Int=0
    val signUpBundle = Bundle()

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
                Log.d("signupStudent","id:${signUpBundle.getString("signUpId")}")
                Log.d("signupStudent","pw:${signUpBundle.getString("signUpPw")}")
                val intent = Intent(this,LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}