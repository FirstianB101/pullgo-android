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
import com.harry.pullgo.ui.dialog.OneButtonDialog
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import com.harry.pullgo.ui.login.LoginActivity
import lib.kingja.switchbutton.SwitchMultiButton.OnSwitchListener
import java.util.regex.Pattern


class FindAccountActivity : AppCompatActivity(), OnSwitchListener{
    private val binding by lazy{ActivityFindAccountBinding.inflate(layoutInflater)}
    private val FIND_ID=0
    private val FIND_PW=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners(){
        binding.switchFindIdPassword.setOnSwitchListener(this)

        binding.buttonFindUsername.setOnClickListener {
            if(checkFindUsernameInputs()) {
                makeDialogSendId(this@FindAccountActivity)
            } //서버 아이디 확인 작업 추가 요함
            else Snackbar.make(binding.root,"정보를 올바르게 입력해주세요!",Snackbar.LENGTH_SHORT).show()
        }

        binding.buttonFindPassword.setOnClickListener {
            if(checkFindPasswordInputs())makeDialogSendPw(this@FindAccountActivity)
            else Snackbar.make(binding.root,"정보를 올바르게 입력해주세요!",Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onSwitch(position: Int, tabText: String?) {
        if (position == FIND_ID) {
            binding.layoutFindAccountFindUsername.visibility = View.VISIBLE
            binding.layoutFindAccountFindPassword.visibility = View.INVISIBLE
        } else if (position == FIND_PW) {
            binding.layoutFindAccountFindUsername.visibility = View.INVISIBLE
            binding.layoutFindAccountFindPassword.visibility = View.VISIBLE
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
        val dialog = TwoButtonDialog(context)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                binding.textInputEditTextFindUsernameFullName.text=null
                binding.textInputEditTextFindUsernamePhone.text=null
                binding.switchFindIdPassword.selectedTab=FIND_PW
                onSwitch(FIND_PW,null)
            }
        }
        dialog.rightClickListener = object: TwoButtonDialog.TwoButtonDialogRightClickListener{
            override fun onRightClicked() {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        dialog.start("아이디 찾기","입력하신 번호로 아이디가 전송되었습니다","비밀번호 찾기","로그인 화면으로")
    }

    private fun makeDialogSendPw(context: Context){
        val dialog = OneButtonDialog(this)
        dialog.centerClickListener = object: OneButtonDialog.OneButtonDialogClickListener{
            override fun onCenterClicked() {
                val intent=Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }
        dialog.start("비밀번호 찾기","임시 비밀번호가 전송되었습니다","로그인 화면으로")
    }
}