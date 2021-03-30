package com.harry.pullgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harry.pullgo.R
import com.harry.pullgo.databinding.ActivityFindAcademyBinding
import com.lakue.lakuepopupactivity.PopupActivity
import com.lakue.lakuepopupactivity.PopupGravity
import com.lakue.lakuepopupactivity.PopupType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FindAcademyActivity : AppCompatActivity() {
    private val binding by lazy{ActivityFindAcademyBinding.inflate(layoutInflater)}
    private var selectedAcademy:Academy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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
            academyAdapter.itemClickListener=object: OnAcademyClick{
                override fun onAcademyClick(view: View,academy:Academy?) {
                    selectedAcademy=academy
                    makeAcceptRequestActivity(view,academy)
                }
            }
            binding.findAcademyRecyclerView.adapter=academyAdapter
        }
    }

    private fun makeAcceptRequestActivity(view: View,academy: Academy?){
        val intent= Intent(view.context, PopupActivity::class.java)
        intent.putExtra("type", PopupType.SELECT)
        intent.putExtra("gravity", PopupGravity.LEFT)
        intent.putExtra("title", "${academy?.name} 학원 가입")
        intent.putExtra("content", academy?.address)
        intent.putExtra("buttonLeft", "가입 요청")
        intent.putExtra("buttonRight", "취소")
        startActivityForResult(intent,2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==2){
                sendAcceptRequest(selectedAcademy?.id)
            }
        }
    }

    private suspend fun getAcademies(input:String): List<Academy>?{
        val retrofit = RetrofitClient.getInstance()
        val service = retrofit.create(RetrofitService::class.java)

        return service.getSuchAcademies(input).execute().body()
    }

    private fun sendAcceptRequest(academyId:Long?){
        val retrofit = RetrofitClient.getInstance()
        val service = retrofit.create(RetrofitService::class.java)

        if (academyId != null) {
            service.sendStudentApplyAcademyRequest(1,academyId).execute()
        }
    }
}