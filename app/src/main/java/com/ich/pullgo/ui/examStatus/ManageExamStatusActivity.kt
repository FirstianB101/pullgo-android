package com.ich.pullgo.ui.examStatus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ich.pullgo.data.adapter.ExamStatusAdapter
import com.ich.pullgo.data.models.AttenderState
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityManageExamStatusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageExamStatusActivity : AppCompatActivity() {
    private val binding by lazy{ActivityManageExamStatusBinding.inflate(layoutInflater)}

    private val viewModel: ManageExamStatusViewModel by viewModels()

    private val studentInfoMap = mutableMapOf<Long,Student>()
    private lateinit var states: List<AttenderState>
    private lateinit var studentsInClassroom: List<Student>

    private lateinit var selectedExam: Exam
    private var selectedClassroomId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        initViewModel()
    }

    private fun initialize(){
        selectedExam = intent.getSerializableExtra("selectedExam") as Exam
        selectedClassroomId = intent.getLongExtra("selectedClassroomId",0L)

        binding.toolbarExamStatus.title = selectedExam.name.toString()
    }

    private fun initViewModel(){
        viewModel.studentsInClassroom.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    studentsInClassroom = it.data!!
                    viewModel.requestGetAttenderStatesInExam(selectedExam.id!!)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(this,"정보를 불러오지 못했습니다 ${it.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.attenderStatesInExam.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    states = it.data!!
                    displayStatus()
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }


        viewModel.getStudentsInClassroom(selectedClassroomId!!)
    }

    private fun displayStatus(){
        val statesMap = mutableMapOf<Long,AttenderState>()

        for(state in states){
            statesMap[state.attenderId!!] = state
        }

        val adapter = ExamStatusAdapter(studentsInClassroom,statesMap,this)

        binding.recyclerViewManageExamStatus.adapter = adapter
    }
}