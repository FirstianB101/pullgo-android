package com.harry.pullgo.ui.studentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harry.pullgo.R
import com.harry.pullgo.data.adapter.ExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.objects.Exam
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ExamsRepository
import com.harry.pullgo.databinding.FragmentStudentExamHistoryBinding

class StudentExamHistoryFragment : Fragment(){
    private val binding by lazy{ FragmentStudentExamHistoryBinding.inflate(layoutInflater)}

    private val viewModel: StudentExamListViewModel by viewModels{StudentExamListViewModelFactory(ExamsRepository())}

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
            displayExams()
        }
    }

    private fun filterExams(case: Int){
        when(case){
            0 -> viewModel.requestExamsByName(LoginInfo.loginStudent?.id!!)
            1 -> viewModel.requestExamsByBeginDate(LoginInfo.loginStudent?.id!!)
            2 -> viewModel.requestExamsByEndDateDesc(LoginInfo.loginStudent?.id!!)
        }
    }

    private fun displayExams(){
        val data = viewModel.studentExamList.value

        val examsAdapter = data?.let {
            ExamAdapter(it)
        }

        if (examsAdapter != null) {
            examsAdapter.itemClickListenerListener = object: OnExamClickListener {
                override fun onExamClick(view: View, exam: Exam?) {
                    selectedExam = exam
                }
            }
        }
        binding.recyclerViewExamHistory.adapter = examsAdapter
    }
}