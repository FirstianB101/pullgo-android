package com.harry.pullgo.ui.login;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.objects.LoadingDialog
import com.harry.pullgo.databinding.ActivityLoginBinding
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.LoginRepository
import com.harry.pullgo.ui.commonFragment.LoadingDialogFragment
import com.harry.pullgo.ui.findAccount.FindAccountActivity
import com.harry.pullgo.ui.signUp.SignUpActivity
import com.harry.pullgo.ui.main.StudentMainActivity
import com.harry.pullgo.ui.main.TeacherMainActivity

class LoginActivity: AppCompatActivity(){
    private val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}

    private val viewModel: LoginViewModel by viewModels{LoginViewModelFactory(LoginRepository(applicationContext))}

    var autoLoginToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //autoLogin()
        setClickListeners()
        initViewModel()
    }

    private fun autoLogin(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        autoLoginToken = pref.getString("token",null)

        if(autoLoginToken != null){
            LoginInfo.user?.token = autoLoginToken

            viewModel.requestAuthorize()
        }
    }

    private fun initViewModel(){
        viewModel.loginUserRepositories.observe(this){
            LoginInfo.user = it

            if(binding.checkBoxAutoLogin.isChecked){
                saveAutoLoginInfo()
            }else{
                resetAutoLoginInfo()
            }

            if(it.student != null)
                viewModel.requestStudentAcademies(it.student?.id!!)
            else if(it.teacher != null)
                viewModel.requestTeacherAcademies(it.teacher?.id!!)
        }

        viewModel.academyRepositoryStudentApplied.observe(this){
            val studentAcademies = viewModel.academyRepositoryStudentApplied.value

            val mainIntent = Intent(applicationContext, StudentMainActivity::class.java)

            if(studentAcademies?.isNotEmpty() == true)
                mainIntent.putExtra("appliedAcademyExist",true)

            LoadingDialog.dialog.dismiss()
            startActivity(mainIntent)
        }

        viewModel.academyRepositoryTeacherApplied.observe(this){
            val teacherAcademies = viewModel.academyRepositoryTeacherApplied.value

            val mainIntent = Intent(applicationContext, TeacherMainActivity::class.java)

            if(teacherAcademies?.isNotEmpty() == true)
                mainIntent.putExtra("appliedAcademyExist",true)

            LoadingDialog.dialog.dismiss()
            startActivity(mainIntent)
        }

        viewModel.loginMessage.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            LoadingDialog.dialog.dismiss()
        }
    }

    private fun saveAutoLoginInfo(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val autoLogin = pref.edit()

        autoLogin.putString("token",LoginInfo.user?.token)
        autoLogin.apply()
    }

    private fun resetAutoLoginInfo(){
        val pref = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        val autoLogin = pref.edit()

        autoLogin.putString("token",null)
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
            LoadingDialog.dialog.show(supportFragmentManager,LoadingDialog.loadingDialogStr)
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