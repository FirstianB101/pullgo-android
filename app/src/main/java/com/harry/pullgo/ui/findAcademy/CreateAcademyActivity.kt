package com.harry.pullgo.ui.findAcademy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.FindAcademyRepository
import com.harry.pullgo.databinding.ActivityCreateAcademyBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityCreateAcademyBinding.inflate(layoutInflater)}

    private val viewModel: FindAcademyViewModel by viewModels{FindAcademyViewModelFactory(
        FindAcademyRepository(LoginInfo.user?.token!!)
    )}

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
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            if(it == "학원을 생성하였습니다") finish()
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
        dialog.start("학원 생성","","생성하기","취소")

    }

    private fun createAcademy(){
        val name = binding.textCreateAcademyName.text.toString()
        val address = "${binding.textCreateAcademyAddress.text.toString()} ${binding.textCreateAcademyDetailedAddress.text.toString()}"
        val phone = binding.textCreateAcademyPhone.text.toString()

        val newAcademy = Academy(null,name,phone,address,LoginInfo.user?.teacher?.id)
        
        viewModel.createAcademy(newAcademy)
    }
}