package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.manage_classroom.manage_request.ManageClassroomManageRequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomManageRequestViewModel @Inject constructor(
    private val manageRequestUseCases: ManageClassroomManageRequestUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageClassroomManageRequestState>(ManageClassroomManageRequestState.Normal)
    val state: StateFlow<ManageClassroomManageRequestState> = _state

    fun getStudentRequests(classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.getStudentRequests(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageRequestState.GetStudentRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeacherRequests(classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.getTeacherRequests(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageRequestState.GetTeacherRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun acceptStudent(classroomId: Long, studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.acceptStudent(classroomId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageRequestState.AcceptRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun acceptTeacher(classroomId: Long, teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.acceptTeacher(classroomId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageRequestState.AcceptRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun denyStudent(classroomId: Long, studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.denyStudent(classroomId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageRequestState.DenyRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun denyTeacher(classroomId: Long, teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.denyTeacher(classroomId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageRequestState.DenyRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageClassroomManageRequestState.Normal)
    }
}