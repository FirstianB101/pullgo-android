package com.harry.pullgo.ui.signUp;

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.harry.pullgo.*
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.databinding.ActivitySignUpStudentBinding
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.repository.SignUpRepository
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

    private val viewModel: SignUpViewModel by viewModels{
        SignUpViewModelFactory(SignUpRepository(app.loginUser.token))
    }

    var curPosition: Int = 0

    private val app: PullgoApplication by lazy{application as PullgoApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initialize(){
        signUpSignUpInfoFragment = StudentSignUpInfoFragment()
        signUpPw = FragmentSignUpPw()
        signUpId = FragmentSignUpId(false)
        supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpId).commit()
    }

    private fun initViewModel(){
        viewModel.signUpId.observe(this){
            selectFragment(1)
        }

        viewModel.signUpPw.observe(this){
            selectFragment(2)
        }

        viewModel.signUpStudent.observe(this){
            viewModel.createStudent(it)
        }

        viewModel.createMessage.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            if(it == "계정이 생성되었습니다"){
                makePopup()
            }
        }
    }

    override fun onBackPressed() {
        if(curPosition == 0){
            super.onBackPressed()
        }else{
            selectFragment(curPosition-1)
        }
    }

    private fun selectFragment(position: Int) {
        var curFragment : Fragment? = null
        curPosition = position
        when(curPosition){
            0 -> {//아이디 입력 프래그먼트
                curFragment = signUpId
            }
            1 -> {//아이디 입력 프래그먼트에서 패스워드 입력으로 넘겨달라 요청
                curFragment = signUpPw
            }
            2 -> {//패스워드 입력 프래그먼트에서 정보 입력으로 넘겨달라 요청
                curFragment = signUpSignUpInfoFragment
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