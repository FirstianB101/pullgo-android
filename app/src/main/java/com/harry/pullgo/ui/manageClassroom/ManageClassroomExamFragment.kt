package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harry.pullgo.data.adapter.ExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.FragmentManageClassroomManageExamBinding

class ManageClassroomExamFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageExamBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by viewModels{
        ManageClassroomViewModelFactory(ManageClassroomRepository(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        setListeners()
        initViewModel()

        return binding.root
    }

    private fun setListeners(){
        binding.floatingActionButtonManageClassroomExams.setOnClickListener {
            FragmentCreateExamDialog(selectedClassroom).show(childFragmentManager,"createExam")
        }
    }

    private fun initViewModel(){
        viewModel.examsWithinClassroom.observe(requireActivity()){
            displayExams(it)
        }

        viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
    }

    private fun displayExams(exams: List<Exam>){
        val examsAdapter = ExamAdapter(exams)

        examsAdapter.itemClickListenerListener = object: OnExamClickListener{
            override fun onExamClick(view: View, exam: Exam?) {

            }
        }

        changeVisibilityIfNoExam(exams.isEmpty())

        binding.recyclerViewManageClassroomExams.adapter = examsAdapter
    }

    private fun changeVisibilityIfNoExam(isEmpty: Boolean){
        if(isEmpty){
            binding.textViewManageClassroomNoExam.visibility = View.VISIBLE
        }else{
            binding.textViewManageClassroomNoExam.visibility = View.GONE
        }
    }
}