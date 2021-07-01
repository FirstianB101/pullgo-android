package com.harry.pullgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.databinding.ActivityFindAcademyBinding
import com.harry.pullgo.interfaces.OnAcademyClick
import com.harry.pullgo.interfaces.RetrofitClient
import com.harry.pullgo.interfaces.RetrofitService
import com.harry.pullgo.objects.Academy
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

class FindAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityFindAcademyBinding.inflate(layoutInflater)}
    private var selectedAcademy: Academy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarFindAcademy.title="학원 찾기"
        binding.findAcademyRecyclerView.layoutManager=LinearLayoutManager(this)

        binding.buttonFindAcademySearch.setOnClickListener {
            val input=binding.findAcademySearchText.text.toString()
            displayAcademies(input)
        }
    }

    private fun displayAcademies(input:String){
        CoroutineScope(Dispatchers.Main).launch {
            var data: List<Academy>? = null
            CoroutineScope(Dispatchers.IO).async{
                data=getAcademies(input)
            }.await()

            val academyAdapter=AcademySearchAdapter(data)
            academyAdapter.itemClickListener=object: OnAcademyClick {
                override fun onAcademyClick(view: View,academy: Academy?) {
                    selectedAcademy=academy
                    makeAcceptRequestActivity(view,academy)
                }
            }
            binding.findAcademyRecyclerView.adapter=academyAdapter
        }
    }

    private fun makeAcceptRequestActivity(view: View,academy: Academy?){
        val intent= Intent(applicationContext, PopupActivity::class.java)
        intent.putExtra("type", PopupType.SELECT)
        intent.putExtra("gravity", PopupGravity.LEFT)
        intent.putExtra("title", "${academy?.name} 학원 가입")
        intent.putExtra("content", academy?.address)
        intent.putExtra("buttonRight", "가입 요청")
        intent.putExtra("buttonLeft", "취소")
        startActivityForResult(intent,2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==2){
                val result=data?.getSerializableExtra("result") as PopupResult
                if(result==PopupResult.RIGHT)
                    sendAcceptRequest(selectedAcademy?.id)
            }
        }
    }

    private suspend fun getAcademies(input:String): List<Academy>?{
        val service = RetrofitClient.getApiService()

        return service.getSuchAcademies(input).execute().body()
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