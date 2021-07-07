package com.harry.pullgo.ui.findAcademy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.databinding.ActivityFindAcademyBinding
import com.harry.pullgo.data.api.OnAcademyClick
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.repository.FindAcademyRepository
import com.harry.pullgo.ui.AcademySearchAdapter
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupResult
import com.lakue.lakuepopupactivity.PopupType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.Provider

class FindAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityFindAcademyBinding.inflate(layoutInflater)}
    private lateinit var viewModel: FindAcademyViewModel
    private var selectedAcademy: Academy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        setListeners()

        binding.toolbarFindAcademy.title = "학원 찾기"
        binding.findAcademyRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initViewModel(){
        val viewModelFactory = FindAcademyViewModelFactory(FindAcademyRepository())
        viewModel = ViewModelProvider(this,viewModelFactory).get(FindAcademyViewModel::class.java)

        viewModel.findAcademyRepositories.observe(this){
            displayAcademies()
        }
    }

    private fun setListeners(){
        binding.buttonFindAcademySearch.setOnClickListener {
            viewModel.requestGetAcademies(binding.findAcademySearchText.text.toString())
        }
    }

    private fun displayAcademies(){
        val data = viewModel.findAcademyRepositories.value

        val academyAdapter = AcademySearchAdapter(data)
        academyAdapter.itemClickListener = object: OnAcademyClick {
            override fun onAcademyClick(view: View,academy: Academy?) {
                selectedAcademy=academy
                showEnrollRequestDialog(academy)
            }
        }
        binding.findAcademyRecyclerView.adapter = academyAdapter
    }

    private fun showEnrollRequestDialog(academy: Academy?){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                sendAcceptRequest(selectedAcademy?.id)
            }
        }
        dialog.start("${academy?.name}","${academy?.address}","가입 요청","취소")
    }

    private fun sendAcceptRequest(academyId:Long?){
        val service = RetrofitClient.getApiService()

        if (academyId != null) {
            // 회원가입 이후 학생 ID 넣어 요청보내기
            service.sendStudentApplyAcademyRequest(4,academyId).enqueue(object: Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if(response.isSuccessful){
                        Snackbar.make(binding.root,"요청 성공",Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                }
            })
        }
    }
}