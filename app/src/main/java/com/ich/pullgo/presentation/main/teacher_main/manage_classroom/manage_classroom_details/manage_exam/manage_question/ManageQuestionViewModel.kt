package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.manage_question.ManageQuestionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ManageQuestionViewModel @Inject constructor(
    private val manageQuestionUseCases: ManageQuestionUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageQuestionState>(ManageQuestionState.Normal)
    val state: StateFlow<ManageQuestionState> = _state

    fun createQuestion(question: Question) = viewModelScope.launch {
        manageQuestionUseCases.createQuestion(question).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageQuestionState.CreateQuestion(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageQuestionState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageQuestionState.Error(result.message.toString())
                }
            }
        }
    }

    fun editQuestion(questionId: Long, question: Question) = viewModelScope.launch {
        manageQuestionUseCases.editQuestion(questionId, question).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageQuestionState.EditQuestion(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageQuestionState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageQuestionState.Error(result.message.toString())
                }
            }
        }
    }

    fun deleteQuestion(questionId: Long) = viewModelScope.launch {
        manageQuestionUseCases.deleteQuestion(questionId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageQuestionState.DeleteQuestion
                }
                is Resource.Loading -> {
                    _state.value = ManageQuestionState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageQuestionState.Error(result.message.toString())
                }
            }
        }
    }

    fun getQuestionsInExam(examId: Long) = viewModelScope.launch {
        manageQuestionUseCases.getQuestions(examId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageQuestionState.GetQuestions(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageQuestionState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageQuestionState.Error(result.message.toString())
                }
            }
        }
    }

    fun uploadImage(image: RequestBody) = viewModelScope.launch {
        manageQuestionUseCases.uploadImage(image).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageQuestionState.UploadImage(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageQuestionState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageQuestionState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageQuestionState.Normal)
    }
}