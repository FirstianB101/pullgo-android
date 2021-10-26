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
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ExamsRepository
import com.harry.pullgo.databinding.FragmentStudentExamListBinding

class StudentExamListFragment : Fragment(){
    private val binding by lazy{FragmentStudentExamListBinding.inflate(layoutInflater)}

    private val viewModel: StudentExamListViewModel by viewModels{
        StudentExamListViewModelFactory(ExamsRepository(requireContext(), app.loginUser.token))
    }

    private var selectedExam: Exam? = null

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
        initViewModel()
        return binding.root
    }

    private fun initialize(){
        ArrayAdapter.createFromResource(requireContext(),R.array.exam_list_filter,android.R.layout.simple_spinner_item)
            .also { adapter->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerExamList.adapter = adapter
            }

        binding.spinnerExamList.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterExams(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.recyclerViewExamList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initViewModel(){
        viewModel.studentExamList.observe(requireActivity()){
            displayExams()
        }
    }

    private fun filterExams(case: Int){
        when(case){
            0 -> viewModel.requestExamsByName(app.loginUser.student?.id!!)
            1 -> viewModel.requestExamsByBeginDate(app.loginUser.student?.id!!)
            2 -> viewModel.requestExamsByEndDate(app.loginUser.student?.id!!)
        }
        app.showLoadingDialog(childFragmentManager)
    }

    private fun displayExams(){
        val data = viewModel.studentExamList.value

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
        binding.recyclerViewExamList.adapter = examsAdapter
        app.dismissLoadingDialog()
    }
}