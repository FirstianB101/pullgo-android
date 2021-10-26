package com.harry.pullgo.ui.findAcademy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.AcademyAdapter
import com.harry.pullgo.databinding.ActivityFindAcademyBinding
import com.harry.pullgo.data.api.OnAcademyClickListener
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.repository.FindAcademyRepository
import com.harry.pullgo.ui.dialog.TwoButtonDialog

class FindAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityFindAcademyBinding.inflate(layoutInflater)}
    private val viewModel: FindAcademyViewModel by viewModels{
        FindAcademyViewModelFactory(FindAcademyRepository(applicationContext, app.loginUser.token))
    }

    private val app: PullgoApplication by lazy{application as PullgoApplication }

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
            displayAcademies()
           app.dismissLoadingDialog()
        }

        viewModel.requestMessage.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            app.dismissLoadingDialog()
        }
    }

    private fun setListeners(){
        binding.buttonFindAcademySearch.setOnClickListener {
            viewModel.requestGetAcademies(binding.searchTextFindAcademy.text.toString())
            app.showLoadingDialog(supportFragmentManager)
        }

        binding.floatingActionButtonMakeAcademy.setOnClickListener {
            val intent = Intent(this,CreateAcademyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayAcademies(){
        val data = viewModel.findAcademyRepositories.value

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