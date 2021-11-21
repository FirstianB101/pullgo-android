package com.harry.pullgo.ui.manageQuestion;

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.harry.pullgo.R
import com.harry.pullgo.application.PullgoApplication
import com.harry.pullgo.data.api.OnEditMultipleChoiceListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.ImageUtil
import com.harry.pullgo.data.utils.Status
import com.harry.pullgo.databinding.ActivityManageQuestionBinding
import com.harry.pullgo.ui.dialog.TwoButtonDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

@AndroidEntryPoint
class ManageQuestionActivity: AppCompatActivity() {
    private val binding by lazy{ActivityManageQuestionBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ManageQuestionViewModel by viewModels()

    private lateinit var selectedExam: Exam

    private lateinit var questions: List<Question>
    private lateinit var questionFragments: MutableList<FragmentManageQuestion>
    private var curPos = 0

    private lateinit var startForResult: ActivityResultLauncher<Intent>

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
            if(questions.isNotEmpty()){
                questions[curPos].content = questionFragments[curPos].getCurrentContent()
                if(questionFragments[curPos].isImageChanged) {
                    uploadImage(questionFragments[curPos].getCurImageUrl())
                }else{
                    viewModel.editQuestion(questions[curPos].id!!, questions[curPos])
                }
            }
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

        binding.pagerManageQuestion.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                curPos = position
                binding.topAppBar.title = "문제 ${curPos + 1}"
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

                    if(questions.isEmpty())
                        initNoQuestionFragment()
                    else
                        initQuestionFragmentsPager(questions.size)
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

        viewModel.imageUploadRepository.observe(this){
            when(it.status){
                Status.LOADING -> {
                    app.showLoadingDialog(supportFragmentManager)
                }
                Status.SUCCESS -> {
                    app.dismissLoadingDialog()
                    questions[curPos].pictureUrl = it.data?.data?.url
                    viewModel.editQuestion(questions[curPos].id!!, questions[curPos])

                    questionFragments[curPos].isImageChanged = false
                }
                Status.ERROR -> {
                    Toast.makeText(this,"이미지를 업로드하지 못하였습니다 (${it.message})",Toast.LENGTH_SHORT).show()
                    app.dismissLoadingDialog()
                }
            }
        }
    }

    private fun startAddQuestionActivity(){
        val intent = Intent(this, AddQuestionActivity::class.java)
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
            binding.pagerManageQuestion.currentItem = curPos - 1
        }
    }

    private fun showNextQuestion(){
        if(questions.isNotEmpty() && curPos < questions.size - 1){
            binding.pagerManageQuestion.currentItem = curPos + 1
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

    private fun initNoQuestionFragment(){
        binding.pagerManageQuestion.adapter = ManageQuestionPagerAdapter(this, listOf(FragmentNoQuestion()))
    }

    private fun initQuestionFragmentsPager(size: Int){
        questionFragments = mutableListOf()
        for(i in 0 until size){
            questionFragments.add(FragmentManageQuestion(questions[i]))
        }
        binding.pagerManageQuestion.adapter = ManageQuestionPagerAdapter(this,questionFragments)
        binding.topAppBar.title = "문제 ${curPos+1}"
    }

    override fun onBackPressed() {
        finish()
    }

    class ManageQuestionPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>)
        : FragmentStateAdapter(activity){
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    private fun uploadImage(imageUri: Uri?){
        if(imageUri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val requestBody =
                RequestBody.create(MediaType.parse("text/plain"), ImageUtil.BitmapToString(bitmap))

            viewModel.requestUploadImage(requestBody)
        }
    }
}