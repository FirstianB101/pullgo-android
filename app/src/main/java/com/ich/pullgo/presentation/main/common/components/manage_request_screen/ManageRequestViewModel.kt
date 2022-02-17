package com.ich.pullgo.presentation.main.common.components.manage_request_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.manage_request.ManageRequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageRequestViewModel @Inject constructor(
    private val manageRequestUseCases: ManageRequestUseCases
): ViewModel(){
    private val _state = MutableStateFlow<ManageRequestState>(ManageRequestState.Normal)
    val state: StateFlow<ManageRequestState> = _state

    fun getAcademiesStudentApplying(studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.getAcademiesStudentApplying(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.AcademyRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAcademiesTeacherApplying(teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.getAcademiesTeacherApplying(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.AcademyRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun getClassroomsStudentApplying(studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.getClassroomsStudentApplying(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.ClassroomRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun getClassroomsTeacherApplying(teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.getClassroomsTeacherApplying(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.ClassroomRequests(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun removeStudentApplyingAcademyRequest(studentId: Long, academyId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeStudentApplyingAcademyRequest(studentId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.RemoveAcademyRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun removeTeacherApplyingAcademyRequest(teacherId: Long, academyId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeTeacherApplyingAcademyRequest(teacherId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.RemoveAcademyRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun removeStudentApplyingClassroomRequest(studentId: Long, classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeStudentApplyingClassroomRequest(studentId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.RemoveClassroomRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun removeTeacherApplyingClassroomRequest(teacherId: Long, classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeTeacherApplyingClassroomRequest(teacherId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.RemoveClassroomRequest
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun getOwnerOfAcademy(ownerId: Long) = viewModelScope.launch {
        manageRequestUseCases.getOwnerOfAcademy(ownerId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.GetAcademyOwner(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAcademyOfClassroom(academyId: Long) = viewModelScope.launch {
        manageRequestUseCases.getAcademyOfClassroom(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageRequestState.GetAcademyOfClassroom(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageRequestState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageRequestState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageRequestState.Normal)
    }
}