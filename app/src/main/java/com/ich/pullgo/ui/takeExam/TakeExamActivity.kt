package com.ich.pullgo.ui.takeExam

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.api.OnChoiceListener
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/takeExam/TakeExamActivity.kt
import com.ich.pullgo.data.models.Answer
import com.ich.pullgo.data.models.AttenderState
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.data.utils.DurationUtil
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityTakeExamBinding
=======
import com.ich.pullgo.data.utils.DurationUtil
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.ActivityTakeExamBinding
import com.ich.pullgo.domain.model.Answer
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Question
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/takeExam/TakeExamActivity.kt
import com.ich.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class TakeExamActivity : AppCompatActivity(){
    private val binding by lazy{ActivityTakeExamBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: TakeExamViewModel by viewModels()

    private lateinit var selectedExam: Exam
    private lateinit var selectedState: AttenderState

    private lateinit var questions: List<Question>
    private lateinit var questionFragments: MutableList<Fragment>
    private lateinit var answers: MutableList<MutableList<Int>>

    private var curPos = 0
    private var savedPos = 0

    private var isFirst = true
    private var isFinished = false

    private lateinit var timer: CountDownTimer

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
        selectedState = intent.getSerializableExtra("selectedState") as AttenderState

        viewModel.getQuestionsSuchExam(selectedExam.id!!)
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
                if(!isFirst && !isFinished)
                    saveCurrentAnswer(answers[curPos])

                curPos = position
                isFirst = false
            }
        })
    }

    private fun initViewModel(){
        viewModel.questionsSuchExamRepository.observe(this){
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

        viewModel.attenderAnswerRepository.observe(this){
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

        viewModel.takeExamMessage.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(applicationContext,"시험 응시가 완료되었습니다",Toast.LENGTH_SHORT).show()
                    doFinishWorks()
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
            FragmentMultipleChoiceBottomSheet(questions[curPos] ,answers[curPos] ,object : OnChoiceListener {
                override fun onChoice(choices: List<Int>) {
                    answers[curPos] = choices.toMutableList()
                }
            }).show(supportFragmentManager, "multiple_choice_answer")
        }
    }

    private fun saveCurrentAnswer(answer: List<Int>){
        if(answer.isNotEmpty()) {
            savedPos = curPos + 1
            viewModel.saveAttenderAnswer(selectedState.id!!, questions[curPos].id!!, Answer(answer))
        }
    }

    private fun initQuestionFragmentsPager(size: Int){
        questionFragments = mutableListOf()
        for(i in 0 until size){
            questionFragments.add(FragmentTakeExamQuestion(questions[i],i+1))
        }
        binding.pagerTakeExam.adapter = TakeExamPagerAdapter(this, questionFragments)
    }

    private fun showFinishExamDialog(){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener{
            override fun onLeftClicked() {
                viewModel.submitAttenderState(selectedState.id!!)
            }
        }
        dialog.start("응시 완료","답안들을 제출하고 종료하시겠습니까?","응시 완료","취소")
    }

    private fun doFinishWorks(){
        timer.cancel()
        isFinished = true

        showResultFragment()
        changeFinishButtonClickListener()
        hideActivityLayouts()
    }

    private fun showResultFragment(){
        questionFragments = mutableListOf(FragmentTakeExamResult(selectedState.id!!,questions))
        binding.pagerTakeExam.adapter = TakeExamPagerAdapter(this,questionFragments)
    }

    private fun changeFinishButtonClickListener(){
        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.end_exam ->{
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun hideActivityLayouts(){
        binding.buttonShowMultipleChoice.visibility = View.GONE
        binding.buttonSaveAnswer.visibility = View.GONE
        binding.buttonNextQuiz.visibility = View.GONE
        binding.buttonPreviousQuiz.visibility = View.GONE
    }

    class TakeExamPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>)
        : FragmentStateAdapter(activity){
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    private fun startCountDown(startTime: Long){
        timer = object: CountDownTimer(startTime - 1000,1000){
            override fun onTick(millis: Long) {
                val hour = TimeUnit.MILLISECONDS.toHours(millis)
                val minutes = (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))
                val seconds = (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

                binding.topAppBar.title = String.format("%02d:%02d:%02d",hour,minutes,seconds)
            }

            override fun onFinish() {
                Toast.makeText(applicationContext,"시험시간 종료",Toast.LENGTH_SHORT).show()
                viewModel.submitAttenderState(selectedState.id!!)
            }
        }.start()
    }
}