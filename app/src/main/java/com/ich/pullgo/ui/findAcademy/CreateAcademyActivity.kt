package com.ich.pullgo.ui.findAcademy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityCreateAcademyBinding
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityCreateAcademyBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: FindAcademyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
        initViewModel()
    }

    private fun initialize(){
        binding.textCreateAcademyName.requestFocus()
    }

    private fun setListeners(){
        binding.buttonCreateAcademyCreate.setOnClickListener {
            if(isFilledAllEmpties()){
                showCreateAcademyDialog()
            }else{
                Snackbar.make(binding.root,"입력되지 않은 항목이 존재합니다",Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.buttonCreateAcademyCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun initViewModel(){
        viewModel.createMessage.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(this,it.data,Toast.LENGTH_SHORT).show()

                    val intent = Intent()
                    intent.putExtra("createAcademy","yes")
                    setResult(Activity.RESULT_OK,intent)
                    finish()
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

    private fun isFilledAllEmpties(): Boolean{
        return binding.textCreateAcademyAddress.text.toString() != "" && binding.textCreateAcademyName.text.toString() != ""
                && binding.textCreateAcademyDetailedAddress.text.toString() != "" && binding.textCreateAcademyPhone.text.toString() != ""
    }

    private fun showCreateAcademyDialog(){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                createAcademy()
            }
        }
        val academyName = "학원 이름: ${binding.textCreateAcademyName.text.toString()}\n"
        val academyAddress = "학원 주소: ${binding.textCreateAcademyAddress.text.toString()}\n"
        val academyPhone = "전화번호 : ${binding.textCreateAcademyPhone.text.toString()}\n"
        val comment = "위 정보로 학원을 생성하시겠습니까?"

        dialog.start("학원 생성",academyName + academyAddress + academyPhone + comment,"생성하기","취소")
    }

    private fun createAcademy(){
        val name = binding.textCreateAcademyName.text.toString()
        val address = "${binding.textCreateAcademyAddress.text.toString()} ${binding.textCreateAcademyDetailedAddress.text.toString()}"
        val phone = binding.textCreateAcademyPhone.text.toString()

        val newAcademy = Academy(name,phone,address,app.loginUser.teacher?.id)
        
        viewModel.createAcademy(newAcademy)
    }
}