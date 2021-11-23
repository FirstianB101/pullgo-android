package com.harry.pullgo.ui.examStatus

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ExamStatusAdapter
import com.harry.pullgo.data.models.AttenderState
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityManageExamStatusBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageExamStatusActivity : AppCompatActivity() {
    private val binding by lazy{ActivityManageExamStatusBinding.inflate(layoutInflater)}

    private val viewModel: ManageExamStatusViewModel by viewModels()

    private val studentInfoMap = mutableMapOf<Long,Student>()
    private lateinit var states: List<AttenderState>

    lateinit var selectedExam: Exam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initialize(){
        selectedExam = intent.getSerializableExtra("selectedExam") as Exam

        binding.toolbarExamStatus.title = selectedExam.name.toString()
    }

    private fun initViewModel(){
        viewModel.attenderStatesInExam.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    states = it.data!!
                    fillAllAttendersInfo()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this,"정보를 불러오지 못했습니다 ${it.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.oneStudent.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    studentInfoMap[it.data?.id!!] = it.data

                    if(states.size == studentInfoMap.keys.size) {
                        displayExams()
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }

        viewModel.requestGetAttenderStatesInExam(selectedExam.id!!)
    }

    private fun fillAllAttendersInfo(){
        for(state in states){
            viewModel.getOneStudent(state.attenderId!!)
        }
        if(states.isEmpty()){
            binding.textViewManageExamStatusNoResult.visibility = View.VISIBLE
        }
    }

    private fun displayExams(){
        val exams = viewModel.attenderStatesInExam.value?.data!!

        val adapter = ExamStatusAdapter(exams,studentInfoMap,this)

        binding.recyclerViewManageExamStatus.adapter = adapter
    }
}