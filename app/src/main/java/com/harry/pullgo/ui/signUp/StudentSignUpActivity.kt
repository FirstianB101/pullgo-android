package com.harry.pullgo.ui.signUp;

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.*
import com.harry.pullgo.databinding.ActivitySignUpStudentBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.ui.commonFragment.FragmentSignUpId
import com.harry.pullgo.ui.commonFragment.FragmentSignUpPw
import com.harry.pullgo.ui.dialog.OneButtonDialog
import com.harry.pullgo.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentSignUpActivity:AppCompatActivity(){
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
            selectFragment(1)
        }
        
        viewModel.signUpPw.observe(this){
            selectFragment(2)
        }
        
        viewModel.signUpStudent.observe(this){
            createStudent(viewModel.signUpStudent.value)

            makePopup()
        }
    }

    override fun onBackPressed() {
        if(curPosition==0){
            super.onBackPressed()
        }else{
            selectFragment(curPosition-1)
        }
    }

    private fun selectFragment(position: Int) {
        var curFragment : Fragment? = null
        when(position){
            0 -> {//아이디 입력 프래그먼트
                curFragment = signUpId
                curPosition=0
            }
            1 -> {//아이디 입력 프래그먼트에서 패스워드 입력으로 넘겨달라 요청
                curFragment = signUpPw
                curPosition=1
            }
            2 -> {//패스워드 입력 프래그먼트에서 정보 입력으로 넘겨달라 요청
                curFragment = signUpSignUpInfoFragment
                curPosition=2
            }
        }
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        transaction.replace(R.id.studentSignUpContainer, curFragment!!).addToBackStack(null).commit()
    }

    private fun createStudent(student: Student?){
        if (student != null) {
            val service= RetrofitClient.getApiService()

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
        val dialog = OneButtonDialog(this)
        dialog.centerClickListener = object: OneButtonDialog.OneButtonDialogClickListener{
            override fun onCenterClicked() {
                val intent=Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }
        dialog.start("회원가입이 완료되었습니다!","가입하신 정보로 로그인해주세요","로그인 화면으로")
    }
}