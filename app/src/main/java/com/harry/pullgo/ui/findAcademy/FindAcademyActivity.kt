package com.harry.pullgo.ui.findAcademy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.AcademyAdapter
import com.harry.pullgo.data.api.OnAcademyClickListener
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityFindAcademyBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FindAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityFindAcademyBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: FindAcademyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
        setListeners()
    }

    private fun initialize(){
        if(intent.getBooleanExtra("calledByStudent",false))
            binding.floatingActionButtonMakeAcademy.visibility = View.GONE

        binding.toolbarFindAcademy.title = "학원 찾기"

        binding.recyclerViewFindAcademy.layoutManager = LinearLayoutManager(this)
    }

    private fun initViewModel(){
        viewModel.findAcademyRepositories.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    displayAcademies()
                    app.dismissLoadingDialog()
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()
                }
            }
        }

        viewModel.requestMessage.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(this,"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    app.dismissLoadingDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListeners(){
        binding.buttonFindAcademySearch.setOnClickListener {
            viewModel.requestGetAcademies(binding.searchTextFindAcademy.text.toString())
        }

        binding.floatingActionButtonMakeAcademy.setOnClickListener {
            val intent = Intent(this,CreateAcademyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayAcademies(){
        val data = viewModel.findAcademyRepositories.value?.data

        val academyAdapter = data?.let {
            AcademyAdapter(it)
        }

        if (academyAdapter != null) {
            academyAdapter.itemClickListener = object: OnAcademyClickListener {
                override fun onAcademyClick(view: View,academy: Academy?) {
                    showApplyRequestDialog(academy)
                }
            }
        }
        binding.recyclerViewFindAcademy.adapter = academyAdapter
    }

    private fun showApplyRequestDialog(academy: Academy?){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                sendAcceptRequest(academy?.id)
            }
        }
        dialog.start("${academy?.name}","${academy?.address}","가입 요청","취소")
    }

    private fun sendAcceptRequest(academyId:Long?){
        if (academyId != null) {
            val isStudent = (app.loginUser.teacher == null)

            if(isStudent)
                viewModel.requestStudentApply(app.loginUser.student?.id!!,academyId)
            else
                viewModel.requestTeacherApply(app.loginUser.teacher?.id!!,academyId)
        }
    }
}