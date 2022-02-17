package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.accept_apply_academy.AcceptApplyAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcceptApplyAcademyViewModel @Inject constructor(
    private val acceptApplyAcademyUseCases: AcceptApplyAcademyUseCases
): ViewModel() {
    private val _state = MutableStateFlow<AcceptApplyAcademyState>(AcceptApplyAcademyState.Normal)
    val state: StateFlow<AcceptApplyAcademyState> = _state

    fun getAppliedAcademies(teacherId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.getAcademiesTeacherApplied(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.GetAcademies(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun getStudentRequests(academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.getStudentsApplyingAcademy(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.GetStudentRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeacherRequests(academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.getTeachersApplyingAcademy(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.GetTeacherRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun acceptStudentRequest(studentId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.acceptStudentRequest(academyId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.AcceptRequest
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun acceptTeacherRequest(teacherId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.acceptTeacherRequest(academyId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.AcceptRequest
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun denyStudentRequest(studentId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.denyStudentRequest(academyId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.DenyRequest
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun denyTeacherRequest(teacherId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.denyTeacherRequest(academyId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = AcceptApplyAcademyState.DenyRequest
                }
                is Resource.Loading -> {
                    _state.value = AcceptApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = AcceptApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(AcceptApplyAcademyState.Normal)
    }
}