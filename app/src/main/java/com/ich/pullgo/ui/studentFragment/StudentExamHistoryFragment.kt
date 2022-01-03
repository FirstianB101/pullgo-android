package com.ich.pullgo.ui.studentFragment

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
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.adapter.ExamAdapter
import com.ich.pullgo.data.api.OnExamClickListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/studentFragment/StudentExamHistoryFragment.kt
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.data.utils.ExamProgress
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentStudentExamHistoryBinding
=======
import com.ich.pullgo.data.utils.ExamProgress
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentStudentExamHistoryBinding
import com.ich.pullgo.domain.model.Exam
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/studentFragment/StudentExamHistoryFragment.kt
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import com.ich.pullgo.ui.takenExamHistory.ExamHistoryActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentExamHistoryFragment : Fragment(){
    private val binding by lazy{ FragmentStudentExamHistoryBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private var selectedExam: Exam? = null

    private val viewModel: StudentExamListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize()
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
    }

    private fun initViewModel(){
        viewModel.studentExamList.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    viewModel.getStudentAttenderStates(app.loginUser.student?.id!!)
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.studentAttenderStates.observe(requireActivity()){
            when(it.status){
                Status.SUCCESS -> {
                    displayTakenExams()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"${it.data}(${it.message})", Toast.LENGTH_SHORT).show()
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

    private fun displayTakenExams(){
        val allExams = viewModel.studentExamList.value?.data!!

        val attenderStates = viewModel.studentAttenderStates.value?.data!!

        val takenExams = mutableListOf<Exam>()

        for(state in attenderStates) {
            for (exam in allExams) {
                if (state.progress == ExamProgress.COMPLETE && state.examId == exam.id) {
                    takenExams.add(exam)
                }
            }
        }

        val examsAdapter = ExamAdapter(takenExams)

        examsAdapter.itemClickListener = object: OnExamClickListener {
            override fun onExamClick(view: View, exam: Exam?) {
                selectedExam = exam
                showExamHistoryDialog(exam)
            }

            override fun onRemoveButtonClick(view: View, exam: Exam?) {
            }

            override fun onTakeExamStatusClick(view: View, exam: Exam?) {
            }

            override fun onManageQuestionClick(view: View, exam: Exam?) {
            }
        }
        binding.recyclerViewExamHistory.adapter = examsAdapter

        showNoResultText(takenExams.isEmpty())
    }

    private fun showExamHistoryDialog(exam: Exam?){
        val dialog = TwoButtonDialog(requireContext())
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                showSelectedExamHistory(exam)
            }
        }
        dialog.start("응시한 시험","${exam?.name}시험을 확인하시겠습니까?",
            "확인하기","취소")
    }

    private fun showSelectedExamHistory(exam: Exam?){
        val attenderStates = viewModel.studentAttenderStates.value?.data!!

        for(state in attenderStates){
            if(exam?.id == state.examId){
                startExamHistory(exam,state.id!!)
                break
            }
        }
    }

    private fun startExamHistory(exam: Exam?, attenderStateId: Long){
        val intent = Intent(requireContext(),ExamHistoryActivity::class.java)
        intent.putExtra("selectedExam",exam)
        intent.putExtra("attenderStateId",attenderStateId)
        startActivity(intent)
    }

    private fun showNoResultText(isEmpty: Boolean){
        if(isEmpty)
            binding.textViewStudentNoExamHistory.visibility = View.VISIBLE
        else
            binding.textViewStudentNoExamHistory.visibility = View.GONE
    }
}