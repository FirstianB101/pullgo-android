package com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Answer
import com.ich.pullgo.domain.use_case.take_exam.TakeExamUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TakeExamViewModel @Inject constructor(
    private val takeExamUseCases: TakeExamUseCases
): ViewModel() {
    private val _state = MutableStateFlow<TakeExamState>(TakeExamState.Normal)
    val state: StateFlow<TakeExamState> = _state

    fun getAnAttenderState(attenderStateId: Long) = viewModelScope.launch {
        takeExamUseCases.getAnAttenderState(attenderStateId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = TakeExamState.GetAnAttenderState(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = TakeExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = TakeExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getQuestionsInExam(examId: Long) = viewModelScope.launch {
        takeExamUseCases.getQuestionsInExam(examId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = TakeExamState.GetQuestions(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = TakeExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = TakeExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAttenderAnswers(attenderStateId: Long) = viewModelScope.launch {
        takeExamUseCases.getAttenderAnswers(attenderStateId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = TakeExamState.GetAttenderAnswers(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = TakeExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = TakeExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun saveAttenderAnswer(attenderStateId: Long, questionId: Long, answer: Answer) = viewModelScope.launch {
        takeExamUseCases.saveAttenderAnswer(attenderStateId, questionId, answer).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = TakeExamState.SaveAttenderAnswer(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = TakeExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = TakeExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun submitAttenderState(attenderStateId: Long) = viewModelScope.launch {
        takeExamUseCases.submitAttenderState(attenderStateId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = TakeExamState.SubmitAttenderState
                }
                is Resource.Loading -> {
                    _state.value = TakeExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = TakeExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(TakeExamState.Normal)
    }
}