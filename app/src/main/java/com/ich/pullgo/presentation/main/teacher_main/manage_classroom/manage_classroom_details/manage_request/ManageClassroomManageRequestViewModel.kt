package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.manage_classroom.manage_request.ManageClassroomManageRequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomManageRequestViewModel @Inject constructor(
    private val manageRequestUseCases: ManageClassroomManageRequestUseCases
): ViewModel() {

    private val _state = MutableStateFlow(ManageClassroomManageRequestState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ManageClassroomManageRequestEvent){
        when(event){
            is ManageClassroomManageRequestEvent.SelectStudentRequest -> {
                _state.value = _state.value.copy(
                    selectedStudentRequest = event.student
                )
            }
            is ManageClassroomManageRequestEvent.SelectTeacherRequest -> {
                _state.value = _state.value.copy(
                    selectedTeacherRequest = event.teacher
                )
            }
            is ManageClassroomManageRequestEvent.GetStudentsRequests -> {
                getStudentRequests(event.selectedClassroomId)
            }
            is ManageClassroomManageRequestEvent.GetTeachersRequests -> {
                getTeacherRequests(event.selectedClassroomId)
            }
            is ManageClassroomManageRequestEvent.AcceptStudent -> {
                acceptStudent(event.selectedClassroomId,_state.value.selectedStudentRequest?.id!!)
                    .invokeOnCompletion { getStudentRequests(event.selectedClassroomId) }
            }
            is ManageClassroomManageRequestEvent.DenyStudent -> {
                denyStudent(event.selectedClassroomId,_state.value.selectedStudentRequest?.id!!)
                    .invokeOnCompletion { getStudentRequests(event.selectedClassroomId) }
            }
            is ManageClassroomManageRequestEvent.AcceptTeacher -> {
                acceptTeacher(event.selectedClassroomId,_state.value.selectedTeacherRequest?.id!!)
                    .invokeOnCompletion { getTeacherRequests(event.selectedClassroomId) }
            }
            is ManageClassroomManageRequestEvent.DenyTeacher -> {
                denyTeacher(event.selectedClassroomId,_state.value.selectedTeacherRequest?.id!!)
                    .invokeOnCompletion { getTeacherRequests(event.selectedClassroomId) }
            }
        }
    }

    private fun getStudentRequests(classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.getStudentRequests(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        studentRequests = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getTeacherRequests(classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.getTeacherRequests(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        teacherRequests = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun acceptStudent(classroomId: Long, studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.acceptStudent(classroomId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 승인하였습니다"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 승인하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun acceptTeacher(classroomId: Long, teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.acceptTeacher(classroomId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 승인하였습니다"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 승인하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun denyStudent(classroomId: Long, studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.denyStudent(classroomId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 거절하였습니다"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 거절하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun denyTeacher(classroomId: Long, teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.denyTeacher(classroomId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 거절하였습니다"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("요청을 거절하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
    }
}