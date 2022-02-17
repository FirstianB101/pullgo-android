package com.ich.pullgo.presentation.main.student_main.exam_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.domain.use_case.exam_list.StudentExamListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentExamListViewModel @Inject constructor(
    private val studentExamListUseCases: StudentExamListUseCases
): ViewModel() {
    private val _state = MutableStateFlow<StudentExamListState>(StudentExamListState.Normal)
    val state: StateFlow<StudentExamListState> = _state

    fun getExamsByName(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByName(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = StudentExamListState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = StudentExamListState.Loading
                }
                is Resource.Error -> {
                    _state.value = StudentExamListState.Error(result.message.toString())
                }
            }
        }
    }

    fun getExamsByBeginDate(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByBeginDate(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = StudentExamListState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = StudentExamListState.Loading
                }
                is Resource.Error -> {
                    _state.value = StudentExamListState.Error(result.message.toString())
                }
            }
        }
    }

    fun getExamsByEndDate(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByEndDate(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = StudentExamListState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = StudentExamListState.Loading
                }
                is Resource.Error -> {
                    _state.value = StudentExamListState.Error(result.message.toString())
                }
            }
        }
    }

    fun getExamsByEndDateDesc(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByEndDateDesc(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = StudentExamListState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = StudentExamListState.Loading
                }
                is Resource.Error -> {
                    _state.value = StudentExamListState.Error(result.message.toString())
                }
            }
        }
    }

    fun getStatesByStudentId(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getStatesByStudentId(studentId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = StudentExamListState.GetStates(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = StudentExamListState.Loading
                }
                is Resource.Error -> {
                    _state.value = StudentExamListState.Error(result.message.toString())
                }
            }
        }
    }

    fun startTakingExam(attender: CreateAttender) = viewModelScope.launch {
        studentExamListUseCases.startTakingExam(attender).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = StudentExamListState.StartExam(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = StudentExamListState.Loading
                }
                is Resource.Error -> {
                    _state.value = StudentExamListState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(StudentExamListState.Normal)
    }
}