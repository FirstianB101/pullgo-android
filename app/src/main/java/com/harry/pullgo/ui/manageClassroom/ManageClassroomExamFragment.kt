package com.harry.pullgo.ui.manageClassroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ManageExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ManageClassroomRepository
import com.harry.pullgo.databinding.FragmentManageClassroomManageExamBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageClassroomExamFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageExamBinding.inflate(layoutInflater)}

    private val viewModel: ManageClassroomViewModel by viewModels()

    private val app: PullgoApplication by lazy{requireActivity().application as PullgoApplication }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        setListeners()
        initViewModel()

        return binding.root
    }

    private fun setListeners(){
        binding.floatingActionButtonManageClassroomExams.setOnClickListener {
            FragmentCreateExamDialog(selectedClassroom.id!!).show(childFragmentManager,"createExam")
        }

        setFragmentResultListener("isCreatedExam"){ _, bundle ->
            if(bundle.getString("isCreated") == "yes"){
                viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
            }
        }
    }

    private fun initViewModel(){
        viewModel.examsWithinClassroom.observe(requireActivity()){
            displayExams(it)
        }

        viewModel.manageExamMessage.observe(requireActivity()){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            if(it == "시험이 삭제되었습니다"){
                viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
            }
        }

        viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
    }

    private fun displayExams(exams: List<Exam>){
        val examsAdapter = ManageExamAdapter(exams)

        examsAdapter.examClickListener = object: OnExamClickListener{
            override fun onExamClick(view: View, exam: Exam?) {
                FragmentExamInfoDialog(exam!!).show(childFragmentManager,"examInfo")
            }

            override fun onRemoveButtonClick(view: View, exam: Exam?) {
                showRemoveExamDialog(exam!!)
            }

            override fun onTakeExamStatusClick(view: View, exam: Exam?) {
            }
        }

        changeVisibilityIfNoExam(exams.isEmpty())

        binding.recyclerViewManageClassroomExams.adapter = examsAdapter
    }

    private fun showRemoveExamDialog(exam: Exam){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                viewModel.removeExam(exam.id!!)
            }
        }
        dialog.start("시험 삭제","${exam.name} 시험을 삭제하시겠습니까?","삭제하기","취소")

    }

    private fun changeVisibilityIfNoExam(isEmpty: Boolean){
        if(isEmpty){
            binding.textViewManageClassroomNoExam.visibility = View.VISIBLE
        }else{
            binding.textViewManageClassroomNoExam.visibility = View.GONE
        }
    }
}