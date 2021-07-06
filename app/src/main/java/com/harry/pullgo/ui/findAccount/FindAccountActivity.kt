package com.harry.pullgo.ui.findAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.R
import com.harry.pullgo.databinding.ActivityFindAccountBinding
import com.harry.pullgo.ui.login.LoginActivity
import lib.kingja.switchbutton.SwitchMultiButton.OnSwitchListener
import java.util.regex.Pattern


class FindAccountActivity : AppCompatActivity(), OnSwitchListener ,View.OnClickListener{
    private val binding by lazy{ActivityFindAccountBinding.inflate(layoutInflater)}
    private val FIND_ID=0
    private val FIND_PW=1
    private val PHONE_MIN_LENGTH=9
    private val PHONE_MAX_LENGTH=11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.switchFindIdPassword.setOnSwitchListener(this)
        binding.buttonFindUsername.setOnClickListener(this)
        binding.buttonFindPassword.setOnClickListener(this)
    }

    override fun onSwitch(position: Int, tabText: String?) {
        Log.d("switch","onSwitch")
        if (position == FIND_ID) {
            binding.layoutFindAccountFindUsername.visibility = View.VISIBLE
            binding.layoutFindAccountFindPassword.visibility = View.INVISIBLE
        } else if (position == FIND_PW) {
            binding.layoutFindAccountFindUsername.visibility = View.INVISIBLE
            binding.layoutFindAccountFindPassword.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonFindUsername ->{
                if(checkFindUsernameInputs()) {
                    makeDialogSendId(this@FindAccountActivity)
                } //서버 아이디 확인 작업 추가 요함
                else Snackbar.make(binding.root,"정보를 올바르게 입력해주세요!",Snackbar.LENGTH_SHORT).show()
            }
            R.id.buttonFindPassword ->{
                if(checkFindPasswordInputs())makeDialogSendPw(this@FindAccountActivity)
                else Snackbar.make(binding.root,"정보를 올바르게 입력해주세요!",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun checkFindUsernameInputs():Boolean{
        return binding.textInputEditTextFindUsernameFullName.text.toString()!="" &&
                Pattern.matches("^[0-9]{9,11}$",binding.textInputEditTextFindUsernamePhone.text.toString())
    }

    fun checkFindPasswordInputs():Boolean{
        return binding.textInputEditTextFindPasswordUserName.text.toString()!="" &&
                Pattern.matches("^[0-9]{9,11}$",binding.textInputEditTextFindPasswordPhone.text.toString())
    }

    private fun makeDialogSendId(context: Context){
        val dialog=MaterialDialog(context)
            .positiveButton(null,"비밀번호 찾기")
            .positiveButton {
                binding.textInputEditTextFindUsernameFullName.text=null
                binding.textInputEditTextFindUsernamePhone.text=null
                binding.switchFindIdPassword.selectedTab=FIND_PW
                onSwitch(FIND_PW,null)
            }
            .neutralButton(null,"로그인 화면으로")
            .neutralButton {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            .customView(R.layout.find_account_send_id)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = 800
        lp.height = 500
        dialog.show()
        val window = dialog.window
        window?.attributes=lp
    }

    private fun makeDialogSendPw(context: Context){
        val dialog=MaterialDialog(context)
            .positiveButton(null,"완료")
            .positiveButton {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            .customView(R.layout.find_account_send_pw)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = 800
        lp.height = 500
        dialog.show()
        val window = dialog.window
        window?.attributes=lp
    }
}