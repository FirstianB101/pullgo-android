package com.harry.pullgo.ui.signUp;

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivitySignUpStudentBinding
import com.harry.pullgo.ui.commonFragment.FragmentSignUpId
import com.harry.pullgo.ui.commonFragment.FragmentSignUpPw
import com.harry.pullgo.ui.dialog.OneButtonDialog
import com.harry.pullgo.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentSignUpActivity:AppCompatActivity(){
    private val binding by lazy{ActivitySignUpStudentBinding.inflate(layoutInflater)}
    lateinit var signUpId: FragmentSignUpId
    lateinit var signUpPw: FragmentSignUpPw
    lateinit var signUpSignUpInfoFragment: StudentSignUpInfoFragment

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: SignUpViewModel by viewModels()

    var curPosition: Int = 0

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
        supportFragmentManager.beginTransaction().replace(R.id.studentSignUpContainer,signUpId).addToBackStack(null).commit()
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
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(this,it.data,Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()

                    makePopup()
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    Toast.makeText(this,"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()
                }
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