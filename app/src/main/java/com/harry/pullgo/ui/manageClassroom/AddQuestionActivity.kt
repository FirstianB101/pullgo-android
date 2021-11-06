package com.harry.pullgo.ui.manageClassroom;

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.harry.pullgo.data.api.OnEditMultipleChoiceListener
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.utils.Status

import com.harry.pullgo.databinding.ActivityAddQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddQuestionActivity: AppCompatActivity() {
    private val binding by lazy{ActivityAddQuestionBinding.inflate(layoutInflater)}

    private lateinit var selectedExam: Exam
    private var curImageUri: String? = null
    private var _choice: Map<String, String>? = null
    private var _answer: List<Int>? = null

    private val viewModel: ManageQuestionViewModel by viewModels()

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectedExam = intent.getSerializableExtra("selectedExam") as Exam

        setListeners()
        initViewModel()
    }

    private fun setListeners(){
        binding.imageViewAddQuestion.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startForResult.launch(intent)
        }

        binding.buttonShowAddMultipleChoice.setOnClickListener {
            showMultipleChoiceBottomSheet()
        }

        binding.floatingActionButtonAddQuestion.setOnClickListener {
            val newQuestion = makeQuestion()
            if(newQuestion != null) viewModel.createQuestion(newQuestion)
        }

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                //curImageUri = it.data?.dataString
                curImageUri = "https://avatars.githubusercontent.com/u/77564110?s=200&v=4"
                Glide.with(this)
                    .load(curImageUri)
                    .override(250,400)
                    .fitCenter()
                    .into(binding.imageViewAddQuestion)
            }
        }
    }

    private fun initViewModel(){
        viewModel.createQuestionRepository.observe(this){
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(this,"문제를 생성하였습니다", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("createQuestion","yes")
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(this,"문제를 생성하지 못했습니다 (${it.message})", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMultipleChoiceBottomSheet(){
        val question = Question(_answer,_choice,null,null,selectedExam.id!!)
        FragmentManageQuestionBottomSheet(question, object: OnEditMultipleChoiceListener{
            override fun onEditMultipleChoice(choice: Map<String, String>, answer: List<Int>) {
                _choice = choice
                _answer = answer
            }
        }).show(supportFragmentManager,"add_question_multiple_choice")
    }

    private fun makeQuestion(): Question?{
        val question = Question(null,null,null,null,selectedExam.id)
        if(_choice != null && _answer != null){
            question.pictureUrl = curImageUri
            question.content = binding.editTextAddQuestion.text.toString()
            question.choice = _choice
            question.answer = _answer

            return question
        }else{
            Toast.makeText(this,"입력하지 않은 정보가 존재합니다",Toast.LENGTH_SHORT).show()

            return null
        }
    }
}