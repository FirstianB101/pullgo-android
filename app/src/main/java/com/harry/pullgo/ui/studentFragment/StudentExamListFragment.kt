package com.harry.pullgo.ui.studentFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.adapter.ExamAdapter
import com.harry.pullgo.data.api.OnExamClickListener
import com.harry.pullgo.data.models.AttenderState
import com.harry.pullgo.data.models.CreateAttender
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.FragmentStudentExamListBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import com.harry.pullgo.ui.takeExam.TakeExamActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentExamListFragment : Fragment(){
    private val binding by lazy{FragmentStudentExamListBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: StudentExamListViewModel by viewModels()

    private var selectedExam: Exam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
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
    }

    private fun initViewModel(){
        viewModel.studentExamList.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    viewModel.getStudentAttenderStates(app.loginUser.student?.id!!)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.startExamAttenderState.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    startTakingExam(selectedExam,it.data!!)
                    filterExams(0)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"시험을 응시할 수 없습니다. (${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.studentAttenderStates.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    displayExams()
                }
                Status.LOADING -> {}
                Status.ERROR -> {}
            }
        }
    }

    private fun filterExams(case: Int){
        when(case){
            0 -> viewModel.requestExamsByName(app.loginUser.student?.id!!)
            1 -> viewModel.requestExamsByBeginDate(app.loginUser.student?.id!!)
            2 -> viewModel.requestExamsByEndDate(app.loginUser.student?.id!!)
        }
    }

    private fun displayExams(){
        val exams = viewModel.studentExamList.value?.data!!

        val attenderStates = viewModel.studentAttenderStates.value?.data!!

        val filteredData = mutableListOf<Exam>()

        for(exam in exams){
            if(exam.finished || exam.cancelled)
                continue
            else{
                var exist = false
                for(state in attenderStates){
                    if(exam.id == state.examId) {
                        exist = true
                        break
                    }
                }
                if(!exist)filteredData.add(exam)
            }
        }

        val examsAdapter = ExamAdapter(filteredData)

        examsAdapter.itemClickListener = object: OnExamClickListener {
            override fun onExamClick(view: View, exam: Exam?) {
                selectedExam = exam
                showTakeExamDialog(exam)
            }

            override fun onRemoveButtonClick(view: View, exam: Exam?) {
            }

            override fun onTakeExamStatusClick(view: View, exam: Exam?) {
            }

            override fun onManageQuestionClick(view: View, exam: Exam?) {
            }
        }
        binding.recyclerViewExamList.adapter = examsAdapter

        showNoResultText(filteredData.isEmpty())
    }

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewStudentNoExam.visibility = View.VISIBLE
        else
            binding.textViewStudentNoExam.visibility = View.GONE
    }

    private fun showTakeExamDialog(exam: Exam?){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                val attenderId = app.loginUser.student?.id

                viewModel.startExamByMakingState(CreateAttender(attenderId,exam?.id))
            }
        }
        dialog.start("시험 응시","${exam?.name}시험을 응시하시겠습니까?",
            "응시하기","취소")
    }

    private fun startTakingExam(exam: Exam?, attenderState: AttenderState){
        val intent = Intent(requireContext(),TakeExamActivity::class.java)
        intent.putExtra("selectedExam",exam)
        intent.putExtra("selectedState",attenderState)
        startActivity(intent)
    }
}