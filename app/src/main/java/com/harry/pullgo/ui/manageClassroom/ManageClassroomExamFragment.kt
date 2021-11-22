package com.harry.pullgo.ui.manageClassroom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ManageExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentManageClassroomManageExamBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import com.harry.pullgo.ui.manageQuestion.ManageQuestionActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageClassroomExamFragment(private val selectedClassroom: Classroom): Fragment() {
    private val binding by lazy{FragmentManageClassroomManageExamBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageClassroomViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initialize()
        setListeners()
        initViewModel()

        return binding.root
    }

    private fun initialize(){
        ArrayAdapter.createFromResource(requireContext(), R.array.manage_exam_filter,android.R.layout.simple_spinner_item)
            .also { adapter->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerManageExam.adapter = adapter
            }

        binding.spinnerManageExam.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(binding.spinnerManageExam.selectedItemPosition){
                    0 -> viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
                    1 -> viewModel.requestGetFinishedExams(selectedClassroom.id!!)
                    2 -> viewModel.requestGetCancelledExams(selectedClassroom.id!!)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setListeners(){
        binding.floatingActionButtonManageClassroomExams.setOnClickListener {
            FragmentCreateExamDialog(selectedClassroom.id!!).show(childFragmentManager,"createExam")
        }

        setFragmentResultListener("isExamEdited"){ _, bundle ->
            if(bundle.getString("isEdited") == "yes"){
                viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
            }
        }
    }

    private fun initViewModel(){
        viewModel.examsWithinClassroom.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    displayExams(it.data!!)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.examMessage.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"${it.data}",Toast.LENGTH_SHORT).show()
                    if(it.data == "시험이 삭제되었습니다")
                        viewModel.requestGetExamsWithinClassroom(selectedClassroom.id!!)
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.oneExamInfo.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    FragmentExamInfoDialog(it.data!!).show(childFragmentManager,"examInfo")
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayExams(exams: List<Exam>){
        val examsAdapter: ManageExamAdapter = ManageExamAdapter(exams)

        examsAdapter.examClickListener = object: OnExamClickListener{
            override fun onExamClick(view: View, exam: Exam?) {
                viewModel.getOneExam(exam?.id!!)
            }

            override fun onRemoveButtonClick(view: View, exam: Exam?) {
                showRemoveExamDialog(exam!!)
            }

            override fun onTakeExamStatusClick(view: View, exam: Exam?) {
            }

            override fun onManageQuestionClick(view: View, exam: Exam?) {
                val intent = Intent(requireContext(), ManageQuestionActivity::class.java)
                intent.putExtra("selectedExam",exam)
                startActivity(intent)
            }
        }

        changeVisibilityIfNoExam(examsAdapter.isEmptyList())

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

    private val NO_FILTER = 100
    private val FINISHED = 101
    private val CANCELLED = 102
}