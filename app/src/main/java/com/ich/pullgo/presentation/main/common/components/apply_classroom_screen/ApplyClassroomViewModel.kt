package com.ich.pullgo.presentation.main.common.components.apply_classroom_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.apply_classroom.ApplyClassroomUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyClassroomViewModel @Inject constructor(
    private val applyClassroomUseCases: ApplyClassroomUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ApplyClassroomState>(ApplyClassroomState.Normal)
    val state: StateFlow<ApplyClassroomState> = _state

    fun getStudentAppliedAcademies(studentId: Long) = viewModelScope.launch {
        applyClassroomUseCases.getStudentAppliedAcademies(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyClassroomState.AppliedAcademies(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ApplyClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeacherAppliedAcademies(teacherId: Long) = viewModelScope.launch {
        applyClassroomUseCases.getTeacherAppliedAcademies(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyClassroomState.AppliedAcademies(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ApplyClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun getSearchedClassrooms(academyId: Long, query: String) = viewModelScope.launch {
        applyClassroomUseCases.getSearchedClassrooms(academyId, query).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyClassroomState.SearchedClassrooms(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ApplyClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun sendStudentApplyClassroomRequest(studentId: Long, classroomId: Long) = viewModelScope.launch {
        applyClassroomUseCases.sendStudentApplyClassroomRequest(studentId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyClassroomState.SendApplyClassroomRequest
                }
                is Resource.Loading -> {
                    _state.value = ApplyClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun sendTeacherApplyClassroomRequest(teacherId: Long, classroomId: Long) = viewModelScope.launch {
        applyClassroomUseCases.sendTeacherApplyClassroomRequest(teacherId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyClassroomState.SendApplyClassroomRequest
                }
                is Resource.Loading -> {
                    _state.value = ApplyClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ApplyClassroomState.Normal)
    }
}