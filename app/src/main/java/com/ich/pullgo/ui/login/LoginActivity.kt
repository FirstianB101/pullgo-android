package com.ich.pullgo.ui.login;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityLoginBinding
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.ui.findAccount.FindAccountActivity
import com.ich.pullgo.ui.main.StudentMainActivity
import com.ich.pullgo.ui.main.TeacherMainActivity
import com.ich.pullgo.ui.signUp.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity: AppCompatActivity(){
    private val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: LoginViewModel by viewModels()

    private var isAutoLogin = false
    private var autoLoginToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        autoLogin()
        setClickListeners()
        initViewModel()
    }

    private fun autoLogin(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val authToken = pref.getString("authToken",null)
        autoLoginToken = authToken

        if(authToken != null){
            binding.checkBoxAutoLogin.isChecked = true
            app.loginUser.token = authToken
            isAutoLogin = true
            viewModel.requestAuthorize()
        }
    }

    private fun initViewModel(){
        viewModel.loginUserRepositories.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()
                    app.loginUser = it.data!!

                    if(isAutoLogin) {
                        app.loginUser.token = autoLoginToken
                        isAutoLogin = false
                    }

                    if(binding.checkBoxAutoLogin.isChecked)
                        saveAutoLoginInfo()
                    else
                        resetAutoLoginInfo()

                    if(it.data.student != null)
                        viewModel.requestStudentAcademies(it.data.student?.id!!)
                    else if(it.data.teacher != null)
                        viewModel.requestTeacherAcademies(it.data.teacher?.id!!)
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(this,"연결에 실패했습니다(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.academyRepositoryStudentApplied.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()

                    val studentAcademies = viewModel.academyRepositoryStudentApplied.value?.data

                    val mainIntent = Intent(applicationContext, StudentMainActivity::class.java)

                    if(studentAcademies?.isNotEmpty() == true)
                        mainIntent.putExtra("appliedAcademyExist",true)

                    app.dismissLoadingDialog()
                    startActivity(mainIntent)
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    app.dismissLoadingDialog()
                }
            }
        }

        viewModel.academyRepositoryTeacherApplied.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()

                    val teacherAcademies = viewModel.academyRepositoryTeacherApplied.value?.data

                    val mainIntent = Intent(applicationContext, TeacherMainActivity::class.java)

                    if(teacherAcademies?.isNotEmpty() == true)
                        mainIntent.putExtra("appliedAcademyExist",true)

                    app.dismissLoadingDialog()
                    startActivity(mainIntent)
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    app.dismissLoadingDialog()
                }
            }
        }
    }

    private fun saveAutoLoginInfo(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val autoLogin = pref.edit()

        autoLogin.putString("authToken",app.loginUser.token)
        autoLogin.apply()
    }

    private fun resetAutoLoginInfo(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val autoLogin = pref.edit()

        autoLogin.putString("authToken",null)
        autoLogin.apply()
    }

    private fun setClickListeners(){
        binding.buttonSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        binding.buttonFindAccount.setOnClickListener {
            val intent = Intent(applicationContext, FindAccountActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val account = Account(binding.loginId.text.toString(),null,null,binding.loginPw.text.toString())
            viewModel.requestLogin(account)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PREVIOUS_ID,binding.loginId.text.toString())
        outState.putString(PREVIOUS_PW,binding.loginPw.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.loginId.setText(savedInstanceState.getString(PREVIOUS_ID))
        binding.loginPw.setText(savedInstanceState.getString(PREVIOUS_PW))
    }

    private val PREVIOUS_ID = "previous_id"
    private val PREVIOUS_PW = "previous_pw"
    private val PREVIOUS_IS_STUDENT = "previous_is_student"
}