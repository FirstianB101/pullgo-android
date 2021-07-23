package com.harry.pullgo.ui.findAcademy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.adapter.AcademySearchAdapter
import com.harry.pullgo.databinding.ActivityFindAcademyBinding
import com.harry.pullgo.data.api.OnAcademyClick
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.FindAcademyRepository
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityFindAcademyBinding.inflate(layoutInflater)}
    private lateinit var viewModel: FindAcademyViewModel
    private lateinit var repository: FindAcademyRepository
    private var selectedAcademy: Academy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        setListeners()

        binding.toolbarFindAcademy.title = "학원 찾기"
        binding.recyclerViewFindAcademy.layoutManager = LinearLayoutManager(this)
    }

    private fun initViewModel(){
        repository = FindAcademyRepository()

        val viewModelFactory = FindAcademyViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(FindAcademyViewModel::class.java)

        viewModel.findAcademyRepositories.observe(this){
            displayAcademies()
        }
    }

    private fun setListeners(){
        binding.buttonFindAcademySearch.setOnClickListener {
            viewModel.requestGetAcademies(binding.searchTextFindAcademy.text.toString())
        }
    }

    private fun displayAcademies(){
        val data = viewModel.findAcademyRepositories.value

        val academyAdapter = data?.let {
            AcademySearchAdapter(it)
        }

        if (academyAdapter != null) {
            academyAdapter.itemClickListener = object: OnAcademyClick {
                override fun onAcademyClick(view: View,academy: Academy?) {
                    selectedAcademy=academy
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
                sendAcceptRequest(selectedAcademy?.id)
            }
        }
        dialog.start("${academy?.name}","${academy?.address}","가입 요청","취소")
    }

    private fun sendAcceptRequest(academyId:Long?){
        if (academyId != null) {
            val isStudent = (LoginInfo.loginTeacher == null)
            val service = RetrofitClient.getApiService()

            if(isStudent)
                service.sendStudentApplyAcademyRequest(LoginInfo.loginStudent?.id!!,academyId).enqueue(object: Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if(response.isSuccessful){
                            Snackbar.make(binding.root,"가입 요청이 완료되었습니다",Snackbar.LENGTH_SHORT).show()
                        }else{
                            Snackbar.make(binding.root,"이미 해당 학원에 등록되어 있습니다",Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Snackbar.make(binding.root,"서버 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
                    }
                })
            else
                service.sendTeacherApplyAcademyRequest(LoginInfo.loginTeacher?.id!!,academyId).enqueue(object: Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if(response.isSuccessful){
                            Snackbar.make(binding.root,"가입 요청이 완료되었습니다",Snackbar.LENGTH_SHORT).show()
                        }else{
                            Snackbar.make(binding.root,"이미 해당 학원에 등록되어 있습니다",Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Snackbar.make(binding.root,"서버 연결에 실패했습니다",Snackbar.LENGTH_SHORT).show()
                    }
                })
        }
    }


}