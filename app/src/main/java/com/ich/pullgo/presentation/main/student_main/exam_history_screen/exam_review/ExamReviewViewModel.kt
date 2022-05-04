package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review

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
class ExamReviewViewModel @Inject constructor(
    private val examHistoryUseCases: ExamHistoryUseCases
): ViewModel() {

    private val _state = MutableStateFlow<ExamReviewState>(ExamReviewState.Normal)
    val state: StateFlow<ExamReviewState> = _state

    fun getQuestionsInExam(examId: Long) = viewModelScope.launch {
        examHistoryUseCases.getQuestions(examId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ExamReviewState.GetQuestions(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ExamReviewState.Loading
                }
                is Resource.Error -> {
                    _state.value = ExamReviewState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAttenderAnswers(attenderStateId: Long) = viewModelScope.launch {
        examHistoryUseCases.getAttenderAnswers(attenderStateId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ExamReviewState.GetAttenderAnswers(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ExamReviewState.Loading
                }
                is Resource.Error -> {
                    _state.value = ExamReviewState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ExamReviewState.Normal)
    }
}