package com.harry.pullgo.ui.signUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent
import android.os.Bundle;
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.*

import com.harry.pullgo.databinding.ActivitySignUpTeacherBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.SignUpFragmentSwitch
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.ui.login.LoginActivity
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherSignUpActivity:AppCompatActivity(), SignUpFragmentSwitch {
    private val binding by lazy{ActivitySignUpTeacherBinding.inflate(layoutInflater)}
    lateinit var signUpId: FragmentSignUpId
    lateinit var signUpPw: FragmentSignUpPw
    lateinit var signUpInfoFragment: TeacherSignUpInfoFragment
    lateinit var viewModel: SignUpViewModel

    var curPosition:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize(){
        viewModel= ViewModelProvider(this).get(SignUpViewModel::class.java)
        signUpInfoFragment = TeacherSignUpInfoFragment()
        signUpPw = FragmentSignUpPw()
        signUpId = FragmentSignUpId()
        supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpId).commit()

        viewModel.signUpId.observe(this){
            Log.d("SignUp","[TeacherSignUpActivity]detected ID changed: ${viewModel.signUpId.value}")
        }
        viewModel.signUpPw.observe(this){
            Log.d("SignUp","[TeacherSignUpActivity]detected PW changed: ${viewModel.signUpPw.value}")
        }
        viewModel.signUpTeacher.observe(this){
            Log.d("SignUp","[TeacherSignUpActivity]Teacher Changed: " +
                    "Teacher id: ${viewModel.signUpTeacher.value?.account?.username}\n" +
                    "Teacher phone: ${viewModel.signUpTeacher.value?.account?.phone}\n" +
                    "Teacher fullName: ${viewModel.signUpTeacher.value?.account?.fullName}\n")
        }
    }

    override fun onBackPressed() {
        if(curPosition==0){
            super.onBackPressed()
        }else{
            onDataPass(curPosition-1)
        }
    }

    override fun onDataPass(position: Int) {
        when(position){
            0->{//아이디 입력 프래그먼트
                supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpId).commit()
                curPosition=0
            }
            1->{//아이디 입력 프래그먼트에서 패스워드 입력으로 넘겨달라 요청
                supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpPw).commit()
                curPosition=1
            }
            2->{//패스워드 입력 프래그먼트에서 정보 입력으로 넘겨달라 요청
                supportFragmentManager.beginTransaction().replace(R.id.teacherSignUpContainer,signUpInfoFragment).commit()
                curPosition=2
            }
            3->{//정보 입력 프래그먼트에서 마무리
                createTeacher(viewModel.signUpTeacher.value)

                makePopup()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==1){
                val intent=Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }

    private fun createTeacher(teacher: Teacher?){
        val service= RetrofitClient.getApiService()

        if (teacher != null) {
            service.createTeacher(teacher).enqueue(object: Callback<Teacher> {
                override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                    if(response.isSuccessful){
                        val tea: Teacher? = response.body()
                    }else{
                        Toast.makeText(applicationContext,"계정을 생성하지 못했습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Teacher>, t: Throwable) {
                    Toast.makeText(applicationContext,"서버와 연결에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
            })
        }
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

