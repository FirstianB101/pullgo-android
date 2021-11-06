package com.harry.pullgo.ui.takeExam

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnChoiceListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityTakeExamBinding
import com.harry.pullgo.ui.dialog.OneButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TakeExamActivity : AppCompatActivity(){
    private val binding by lazy{ActivityTakeExamBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: TakeExamViewModel by viewModels()

    private lateinit var selectedExam: Exam

    private lateinit var questions: List<Question>
    private var curPos = 0

    private val LEFT = 100
    private val RIGHT = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initialize()
        setListeners()
        initViewModel()
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

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.end_exam ->{
                    Toast.makeText(this,"시험 종료 클릭",Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun initViewModel(){
        viewModel.questionsSuchExamRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    questions = it.data!!
                    app.dismissLoadingDialog()

                    if(questions.isEmpty())
                        showNoQuestion()
                    else
                        replaceFragment(LEFT)
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
        if(curPos > 0){
            curPos--
            replaceFragment(LEFT)
        }
    }

    private fun showNextQuestion(){
        if(curPos < questions.size){
            curPos++
            replaceFragment(RIGHT)
        }
    }

    private fun showMultipleChoiceFragment(){
        FragmentMultipleChoiceBottomSheet(questions[curPos], object: OnChoiceListener{
            override fun onChoice(choice: Int) {
                Toast.makeText(this@TakeExamActivity,"선택한 보기: $choice",Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun showNoQuestion(){
        val dialog = OneButtonDialog(this)
        dialog.centerClickListener = object: OneButtonDialog.OneButtonDialogClickListener{
            override fun onCenterClicked() {
                finish()
            }
        }
        dialog.start("시험 종료","시험에 등록된 문제가 없습니다","돌아가기")
    }

    private fun replaceFragment(direction: Int){
        val questionFragment = FragmentQuestion(questions[curPos],curPos+1)

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if(direction == LEFT) {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }else if(direction == RIGHT){
            transaction.setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
        }
        transaction.replace(R.id.mainFragmentTakeExam, questionFragment).addToBackStack(null).commit()
    }

}