package com.harry.pullgo.ui.manageClassroom;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnChoiceListener
import com.harry.pullgo.data.api.OnEditMultipleChoiceListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityManageQuestionBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import com.harry.pullgo.ui.takeExam.FragmentMultipleChoiceBottomSheet
import com.harry.pullgo.ui.takeExam.FragmentQuestion
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageQuestionActivity: AppCompatActivity() {
    private val binding by lazy{ActivityManageQuestionBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageQuestionViewModel by viewModels()

    private lateinit var selectedExam: Exam

    private lateinit var questions: List<Question>
    private var curPos = 0

    private val LEFT = 100
    private val RIGHT = 101

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private var curQuestionFragment: FragmentManageQuestion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialize()
        setListeners()
        initViewModel()
    }

    private fun initialize(){
        selectedExam = intent.getSerializableExtra("selectedExam") as Exam

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                if(it.data?.getStringExtra("createQuestion") == "yes"){
                    viewModel.getQuestionsSuchExam(selectedExam.id!!)
                }
            }
        }

        viewModel.getQuestionsSuchExam(selectedExam.id!!)
    }

    private fun setListeners(){
        binding.buttonPreviousQuiz.setOnClickListener {
            showPreviousQuestion()
        }

        binding.buttonNextQuiz.setOnClickListener {
            showNextQuestion()
        }

        binding.buttonShowManageMultipleChoice.setOnClickListener {
            showMultipleChoiceFragment()
        }

        binding.buttonSaveQuestion.setOnClickListener {
            questions[curPos].pictureUrl = curQuestionFragment?.getCurImageUrl()
            questions[curPos].content = curQuestionFragment?.getCurrentContent()
            viewModel.editQuestion(questions[curPos].id!!,questions[curPos])
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete_question ->{
                    if(questions.isEmpty()){
                        Toast.makeText(this,"삭제할 문제가 존재하지 않습니다",Toast.LENGTH_SHORT).show()
                    }else {
                        showDeleteQuestionDialog()
                    }
                    true
                }
                R.id.add_question -> {
                    startAddQuestionActivity()
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
                    curPos = 0
                    app.dismissLoadingDialog()

                    if(questions.isEmpty())
                        showNoQuestionFragment()
                    else
                        replaceQuestionFragment(LEFT)
                }
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.ERROR -> {
                    Toast.makeText(this,"문제 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()
                }
            }
        }

        viewModel.editQuestionRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    saveAtCurQuestionInfo(it.data!!)
                    Toast.makeText(this,"문제 정보가 수정되었습니다",Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this,"문제 정보를 수정하지 못했습니다 (${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deleteQuestionMessage.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(this,"문제가 삭제되었습니다",Toast.LENGTH_SHORT).show()
                    viewModel.getQuestionsSuchExam(selectedExam.id!!)
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this,"문제를 삭제하지 못했습니다 (${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startAddQuestionActivity(){
        val intent = Intent(this,AddQuestionActivity::class.java)
        intent.putExtra("selectedExam",selectedExam)
        startForResult.launch(intent)
    }

    private fun saveAtCurQuestionInfo(question: Question){
        questions[curPos].content = question.content
        questions[curPos].pictureUrl = question.pictureUrl
        questions[curPos].choice = question.choice
        questions[curPos].answer = question.answer
    }

    private fun showDeleteQuestionDialog(){
        val dialog = TwoButtonDialog(this)
        dialog.leftClickListener = object: TwoButtonDialog.TwoButtonDialogLeftClickListener {
            override fun onLeftClicked() {
                viewModel.deleteQuestion(questions[curPos].id!!)
            }
        }
        dialog.start("문제 삭제","문제 ${curPos+1}를 삭제하시겠습니까?",
            "삭제하기","취소")
    }

    private fun showPreviousQuestion(){
        if(questions.isNotEmpty() && curPos > 0){
            curPos--
            replaceQuestionFragment(LEFT)
        }
    }

    private fun showNextQuestion(){
        if(questions.isNotEmpty() && curPos < questions.size - 1){
            curPos++
            replaceQuestionFragment(RIGHT)
        }
    }

    private fun showMultipleChoiceFragment(){
        if(questions.isNotEmpty()) {
            FragmentManageQuestionBottomSheet(questions[curPos], object: OnEditMultipleChoiceListener {
                override fun onEditMultipleChoice(choice: Map<String, String>, answer: List<Int>) {
                    questions[curPos].answer = answer
                    questions[curPos].choice = choice
                }
            }).show(supportFragmentManager,"add_question_multiple_choice")
        }
    }

    private fun showNoQuestionFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentManageQuestion,FragmentNoQuestion()).commit()
    }

    private fun replaceQuestionFragment(direction: Int){
        binding.topAppBar.title = "문제 ${curPos+1}"

        curQuestionFragment = FragmentManageQuestion(questions[curPos])

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if(direction == RIGHT) {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        }else if(direction == LEFT){
            transaction.setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
        }
        transaction.replace(R.id.mainFragmentManageQuestion, curQuestionFragment!!).addToBackStack(null).commit()
    }

}