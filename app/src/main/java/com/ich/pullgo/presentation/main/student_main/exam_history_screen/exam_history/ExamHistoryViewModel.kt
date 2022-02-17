package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.exam_history.ExamHistoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamHistoryViewModel @Inject constructor(
    private val examHistoryUseCases: ExamHistoryUseCases
): ViewModel() {

    private val _state = MutableStateFlow<ExamHistoryState>(ExamHistoryState.Normal)
    val state: StateFlow<ExamHistoryState> = _state

    fun getQuestionsInExam(examId: Long) = viewModelScope.launch {
        examHistoryUseCases.getQuestions(examId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ExamHistoryState.GetQuestions(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ExamHistoryState.Loading
                }
                is Resource.Error -> {
                    _state.value = ExamHistoryState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAttenderAnswers(attenderStateId: Long) = viewModelScope.launch {
        examHistoryUseCases.getAttenderAnswers(attenderStateId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ExamHistoryState.GetAttenderAnswers(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ExamHistoryState.Loading
                }
                is Resource.Error -> {
                    _state.value = ExamHistoryState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ExamHistoryState.Normal)
    }
}