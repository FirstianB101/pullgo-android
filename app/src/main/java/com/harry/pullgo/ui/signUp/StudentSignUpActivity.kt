package com.harry.pullgo.ui.signUp;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.*
import com.harry.pullgo.databinding.ActivitySignUpStudentBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.api.SignUpFragmentSwitch
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.ui.login.LoginActivity
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentSignUpActivity:AppCompatActivity(), SignUpFragmentSwitch {
    private val binding by lazy{ActivitySignUpStudentBinding.inflate(layoutInflater)}
    lateinit var signUpId: FragmentSignUpId
    lateinit var signUpPw: FragmentSignUpPw
    lateinit var signUpSignUpInfoFragment: StudentSignUpInfoFragment
    lateinit var viewModel: SignUpViewModel

    var curPosition:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize(){
        viewModel=ViewModelProvider(this).get(SignUpViewModel::class.java)
        signUpSignUpInfoFragment = StudentSignUpInfoFragment()
        signUpPw = FragmentSignUpPw()
        signUpId = FragmentSignUpId()
        supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpId).commit()

        viewModel.signUpId.observe(this){
            Log.d("SignUp","[SignUpActivity]detected ID changed: ${viewModel.signUpId.value}")
        }
        viewModel.signUpPw.observe(this){
            Log.d("SignUp","[SignUpActivity]detected PW changed: ${viewModel.signUpPw.value}")
        }
        viewModel.signUpStudent.observe(this){
            Log.d("SignUp","[SignUpActivity]Student Changed: " +
                    "Student id: ${viewModel.signUpStudent.value?.account?.username}\n" +
                    "Student phone: ${viewModel.signUpStudent.value?.account?.phone}\n" +
                    "Student fullName: ${viewModel.signUpStudent.value?.account?.fullName}\n" +
                    "Student parentPhone: ${viewModel.signUpStudent.value?.parentPhone}\n" +
                    "Student schoolName: ${viewModel.signUpStudent.value?.schoolName}\n" +
                    "Student schoolYear: ${viewModel.signUpStudent.value?.schoolYear}\n")
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
                supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpId).commit()
                curPosition=0
            }
            1->{//아이디 입력 프래그먼트에서 패스워드 입력으로 넘겨달라 요청
                supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpPw).commit()
                curPosition=1
            }
            2->{//패스워드 입력 프래그먼트에서 정보 입력으로 넘겨달라 요청
                supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpSignUpInfoFragment).commit()
                curPosition=2
            }
            3->{//정보 입력 프래그먼트에서 마무리
                createStudent(viewModel.signUpStudent.value)

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

    private fun createStudent(student: Student?){
        val service= RetrofitClient.getApiService()

        if (student != null) {
            service.createStudent(student).enqueue(object: Callback<Student> {
                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    if(response.isSuccessful){
                        val stu: Student?=response.body()
                    }else{
                        Toast.makeText(applicationContext,"계정을 생성하지 못했습니다",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Student>, t: Throwable) {
                    Toast.makeText(applicationContext,"서버와 연결에 실패했습니다",Toast.LENGTH_SHORT).show()
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