package com.harry.pullgo.ui.takenExamHistory

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnChoiceListener
import com.harry.pullgo.data.models.Answer
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.DurationUtil
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityTakeExamBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ExamHistoryActivity : AppCompatActivity(){
    private val binding by lazy{ActivityTakeExamBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val historyViewModel: ExamHistoryViewModel by viewModels()

    private lateinit var selectedExam: Exam
    private var attenderStateId: Long? = null

    private lateinit var questions: List<Question>
    private lateinit var questionFragmentQuestions: MutableList<FragmentQuestionExamHistory>
    private lateinit var answers: MutableList<MutableList<Int>>

    private var curPos = 0
    private var savedPos = 0

    private var timer = 0L

    private var isFirst = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
        initViewModel()

        startCountDown(DurationUtil.translateDurToMillis(selectedExam.timeLimit!!))
    }

    private fun initialize(){
        selectedExam = intent.getSerializableExtra("selectedExam") as Exam
        attenderStateId = intent.getLongExtra("attenderStateId",0L)

        historyViewModel.getQuestionsSuchExam(selectedExam.id!!)
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

        binding.buttonSaveAnswer.setOnClickListener {
            saveCurrentAnswer(answers[curPos])
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.end_exam ->{
                    showFinishExamDialog()
                    true
                }
                else -> false
            }
        }

        binding.pagerTakeExam.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(!isFirst)
                    saveCurrentAnswer(answers[curPos])

                curPos = position
                isFirst = false
            }
        })
    }

    private fun initViewModel(){
        historyViewModel.questionsSuchExamRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    questions = it.data!!
                    answers = MutableList(questions.size){ mutableListOf()}
                    curPos = 0
                    app.dismissLoadingDialog()

                    if(questions.isNotEmpty())
                        initQuestionFragmentsPager(questions.size)
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

        historyViewModel.attenderAnswerRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(this,"${savedPos}번 답안을 저장했습니다",Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this,"답안을 저장하지 못했습니다 (${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        historyViewModel.takeExamMessage.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(applicationContext,"시험 응시가 완료되었습니다",Toast.LENGTH_SHORT).show()
                    finish()
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    Toast.makeText(applicationContext,"시험 응시를 완료하지 못했습니다",Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun showMultipleChoiceFragment(){
        if(questions.isNotEmpty()) {
            FragmentExamHistoryBottomSheet(questions[curPos] ,answers[curPos] ,object : OnChoiceListener {
                override fun onChoice(choices: List<Int>) {
                    answers[curPos] = choices.toMutableList()
                }
            }).show(supportFragmentManager, "multiple_choice_answer")
        }
    }

    private fun saveCurrentAnswer(answer: List<Int>){
        if(answer.isNotEmpty()) {
            savedPos = curPos + 1
            historyViewModel.saveAttenderAnswer(attenderStateId!!, questions[curPos].id!!, Answer(answer))
        }
    }

    private fun initQuestionFragmentsPager(size: Int){
        questionFragmentQuestions = mutableListOf()
        for(i in 0 until size){
            questionFragmentQuestions.add(FragmentQuestionExamHistory(questions[i],i+1))
        }
        binding.pagerTakeExam.adapter = TakeExamPagerAdapter(this, questionFragmentQuestions)
    }

    private fun showFinishExamDialog(){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                historyViewModel.submitAttenderState(attenderStateId!!)
            }
        }
        dialog.start("응시 완료","답안들을 제출하고 종료하시겠습니까?","응시 완료","취소")
    }

    class TakeExamPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>)
        : FragmentStateAdapter(activity){
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    private fun startCountDown(startTime: Long){
        object: CountDownTimer(startTime,1000){
            override fun onTick(millis: Long) {
                val hour = TimeUnit.MILLISECONDS.toHours(millis)
                val minutes = (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
                val seconds = (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

                binding.topAppBar.title = String.format("%02d:%02d:%02d",hour,minutes,seconds)
                timer = millis
            }

            override fun onFinish() {
                Toast.makeText(applicationContext,"시험시간 종료",Toast.LENGTH_SHORT).show()
                historyViewModel.submitAttenderState(attenderStateId!!)
            }
        }.start()
    }
}