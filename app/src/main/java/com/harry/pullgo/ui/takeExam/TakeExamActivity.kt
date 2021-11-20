package com.harry.pullgo.ui.takeExam

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnChoiceListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.DurationUtil
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityTakeExamBinding
import com.harry.pullgo.ui.dialog.OneButtonDialog
import com.harry.pullgo.ui.manageQuestion.FragmentManageQuestion
import com.harry.pullgo.ui.manageQuestion.ManageQuestionActivity
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

    private lateinit var questions: List<Question>
    private lateinit var questionFragments: MutableList<FragmentTakeExamQuestion>
    private var curPos = 0

    private var timer = 0L

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

        }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.end_exam ->{
                    Toast.makeText(this,"시험 종료 클릭",Toast.LENGTH_SHORT).show()
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
        FragmentMultipleChoiceBottomSheet(questions[curPos], object: OnChoiceListener{
            override fun onChoice(choices: List<Int>) {
                Toast.makeText(this@TakeExamActivity,"선택한 보기: $choices",Toast.LENGTH_SHORT).show()
            }
        }).show(supportFragmentManager,"multiple_choice_answer")
    }

    private fun initQuestionFragmentsPager(size: Int){
        questionFragments = mutableListOf()
        for(i in 0 until size){
            questionFragments.add(FragmentTakeExamQuestion(questions[i],i+1))
        }
        binding.pagerTakeExam.adapter = TakeExamPagerAdapter(this, questionFragments)
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
                // save answers and inform server to finish exam
                Toast.makeText(applicationContext,"시험시간 종료",Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}