package com.harry.pullgo.ui.takenExamHistory

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.models.AttenderAnswer
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.ListCompareUtil
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityTakeExamBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExamHistoryActivity : AppCompatActivity(){
    private val binding by lazy{ActivityTakeExamBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ExamHistoryViewModel by viewModels()

    private lateinit var selectedExam: Exam
    private var attenderStateId: Long? = null

    private lateinit var questions: List<Question>
    private lateinit var questionHistoryFragment: MutableList<FragmentQuestionExamHistory>
    private val answersMap = mutableMapOf<Long,List<Int>>()

    private var curPos = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
        initViewModel()
    }

    private fun initialize(){
        selectedExam = intent.getSerializableExtra("selectedExam") as Exam
        attenderStateId = intent.getLongExtra("attenderStateId",0L)

        binding.buttonSaveAnswer.visibility = View.GONE

        binding.topAppBar.title = selectedExam.name
        binding.buttonShowMultipleChoice.text = "보기 확인"
    }

    private fun setListeners(){
        binding.buttonPreviousQuiz.setOnClickListener {
            showPreviousQuestion()
        }

        binding.buttonNextQuiz.setOnClickListener {
            showNextQuestion()
        }

        binding.buttonShowMultipleChoice.setOnClickListener {
            showMultipleChoiceFragment()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.end_exam ->{
                    showFinishHistoryDialog()
                    true
                }
                else -> false
            }
        }

        binding.pagerTakeExam.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curPos = position
            }
        })
    }

    private fun initViewModel(){
        viewModel.questionsSuchExamRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    questions = it.data!!
                    curPos = 0
                    app.dismissLoadingDialog()

                    if(questions.isNotEmpty()) {
                        initQuestionFragmentsPager(questions.size)
                    }
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    Toast.makeText(this,"문제 정보를 불러오지 못했습니다",Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()
                }
            }
        }

        viewModel.attenderAnswersRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    fillAttenderAnswersMap(it.data!!)
                    viewModel.getQuestionsSuchExam(selectedExam.id!!)
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this,"정답 정보를 불러오지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getAttenderAnswers(attenderStateId!!)
    }

    private fun showPreviousQuestion(){
        if(questions.isNotEmpty() && curPos > 0){
            binding.pagerTakeExam.currentItem = curPos - 1
        }
    }

    private fun showNextQuestion(){
        if(questions.isNotEmpty() && curPos < questions.size - 1){
            binding.pagerTakeExam.currentItem = curPos + 1
        }
    }

    private fun fillAttenderAnswersMap(answers: List<AttenderAnswer>){
        for(answer in answers){
            answersMap[answer.questionId] = answer.answer
        }
    }

    private fun showMultipleChoiceFragment(){
        if(questions.isNotEmpty()) {
            FragmentExamHistoryBottomSheet(questions[curPos],answersMap[questions[curPos].id])
                .show(supportFragmentManager, "multiple_choice_answer")
        }
    }

    private fun initQuestionFragmentsPager(size: Int){
        questionHistoryFragment = mutableListOf()
        for(i in 0 until size){
            val attenderAnswer = answersMap[questions[i].id]

            val isCorrect = if(attenderAnswer == null) false
                            else ListCompareUtil.isEqualList(questions[i].answer!!,attenderAnswer)

            questionHistoryFragment.add(FragmentQuestionExamHistory(questions[i],i+1,isCorrect))
        }
        binding.pagerTakeExam.adapter = ExamHistoryPagerAdapter(this, questionHistoryFragment)
    }

    private fun showFinishHistoryDialog(){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                finish()
            }
        }
        dialog.start("나가기","오답 노트를 그만 보시겠습니까?","나가기","취소")
    }

    class ExamHistoryPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>)
        : FragmentStateAdapter(activity){
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}