package com.harry.pullgo.ui.studentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ExamsRepository
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentStudentExamHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentExamHistoryFragment : Fragment(){
    private val binding by lazy{ FragmentStudentExamHistoryBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: StudentExamListViewModel by viewModels()

    private var selectedExam: Exam? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        initViewModel()
        return binding.root
    }

    private fun initialize(){
        ArrayAdapter.createFromResource(requireContext(),R.array.exam_history_filter,android.R.layout.simple_spinner_item)
            .also { adapter->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerExamHistory.adapter = adapter
            }

        binding.spinnerExamHistory.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExams(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.recyclerViewExamHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initViewModel(){
        viewModel.studentExamList.observe(requireActivity()){
            viewModel.studentExamList.observe(requireActivity()){
                when(it.status){
                    Status.SUCCESS -> {
                        displayExams()
                    }
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun filterExams(case: Int){
        when(case){
            0 -> viewModel.requestExamsByName(app.loginUser.student?.id!!)
            1 -> viewModel.requestExamsByBeginDate(app.loginUser.student?.id!!)
            2 -> viewModel.requestExamsByEndDateDesc(app.loginUser.student?.id!!)
        }
    }

    private fun displayExams(){
        val data = viewModel.studentExamList.value?.data

        val examsAdapter = data?.let {
            ExamAdapter(it)
        }

        if (examsAdapter != null) {
            examsAdapter.itemClickListener = object: OnExamClickListener {
                override fun onExamClick(view: View, exam: Exam?) {
                    selectedExam = exam
                }

                override fun onRemoveButtonClick(view: View, exam: Exam?) {
                }

                override fun onTakeExamStatusClick(view: View, exam: Exam?) {
                }
            }
        }
        binding.recyclerViewExamHistory.adapter = examsAdapter
    }
}