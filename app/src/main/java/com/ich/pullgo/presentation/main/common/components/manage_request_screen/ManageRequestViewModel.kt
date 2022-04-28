package com.ich.pullgo.presentation.main.common.components.manage_request_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.domain.use_case.manage_request.ManageRequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageRequestViewModel @Inject constructor(
    private val manageRequestUseCases: ManageRequestUseCases,
    private val app: PullgoApplication
): ViewModel(){

    private val _state = MutableStateFlow(ManageRequestState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val user by lazy{ app.getLoginUser() }

    fun onEvent(event: ManageRequestEvent){
        when(event){
            is ManageRequestEvent.SelectAcademy -> {
                _state.value = _state.value.copy(
                    selectedAcademy = event.academy
                )
            }
            is ManageRequestEvent.SelectClassroom -> {
                _state.value = _state.value.copy(
                    selectedClassroom = event.classroom
                )
            }
            is ManageRequestEvent.GetAcademyRequests -> {
                user?.doJob(
                    ifStudent = { getAcademiesStudentApplying(user?.student?.id!!) },
                    ifTeacher = { getAcademiesTeacherApplying(user?.teacher?.id!!) }
                )
            }
            is ManageRequestEvent.GetClassroomRequests -> {
                user?.doJob(
                    ifStudent = { getClassroomsStudentApplying(user?.student?.id!!) },
                    ifTeacher = { getClassroomsTeacherApplying(user?.teacher?.id!!) }
                )
            }
            is ManageRequestEvent.RemoveAcademyRequest -> {
                user?.doJob(
                    ifStudent = {
                        removeStudentApplyingAcademyRequest(user?.student?.id!!, _state.value.selectedAcademy?.id!!)
                            .invokeOnCompletion { getAcademiesStudentApplying(user?.student?.id!!) }
                    },
                    ifTeacher = {
                        removeTeacherApplyingAcademyRequest(user?.teacher?.id!!, _state.value.selectedAcademy?.id!!)
                            .invokeOnCompletion { getAcademiesTeacherApplying(user?.teacher?.id!!) }
                    }
                )
            }
            is ManageRequestEvent.RemoveClassroomRequest -> {
                user?.doJob(
                    ifStudent = {
                        removeStudentApplyingClassroomRequest(user?.student?.id!!, _state.value.selectedClassroom?.id!!)
                            .invokeOnCompletion { getClassroomsStudentApplying(user?.student?.id!!) }
                    },
                    ifTeacher = {
                        removeTeacherApplyingClassroomRequest(user?.teacher?.id!!, _state.value.selectedClassroom?.id!!)
                            .invokeOnCompletion { getClassroomsTeacherApplying(user?.teacher?.id!!) }
                    }
                )
            }
            is ManageRequestEvent.ShowAcademyInfo -> {
                getOwnerOfAcademy(_state.value.selectedAcademy?.ownerId!!)
            }
            is ManageRequestEvent.ShowClassroomInfo -> {
                getAcademyOfClassroom(_state.value.selectedClassroom?.academyId!!)
            }
        }
    }

    private fun getAcademiesStudentApplying(studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.getAcademiesStudentApplying(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        academyRequests = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 가져오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getAcademiesTeacherApplying(teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.getAcademiesTeacherApplying(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        academyRequests = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 가져오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getClassroomsStudentApplying(studentId: Long) = viewModelScope.launch {
        manageRequestUseCases.getClassroomsStudentApplying(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        classroomRequests = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 가져오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getClassroomsTeacherApplying(teacherId: Long) = viewModelScope.launch {
        manageRequestUseCases.getClassroomsTeacherApplying(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        classroomRequests = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 가져오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun removeStudentApplyingAcademyRequest(studentId: Long, academyId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeStudentApplyingAcademyRequest(studentId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원 가입 요청을 제거했습니다."))
                    _eventFlow.emit(UiEvent.CloseRemoveDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 제거하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun removeTeacherApplyingAcademyRequest(teacherId: Long, academyId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeTeacherApplyingAcademyRequest(teacherId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원 가입 요청을 제거했습니다."))
                    _eventFlow.emit(UiEvent.CloseRemoveDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 제거하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun removeStudentApplyingClassroomRequest(studentId: Long, classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeStudentApplyingClassroomRequest(studentId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반 가입 요청을 제거했습니다."))
                    _eventFlow.emit(UiEvent.CloseRemoveDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 제거하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun removeTeacherApplyingClassroomRequest(teacherId: Long, classroomId: Long) = viewModelScope.launch {
        manageRequestUseCases.removeTeacherApplyingClassroomRequest(teacherId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반 가입 요청을 제거했습니다."))
                    _eventFlow.emit(UiEvent.CloseRemoveDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("요청을 제거하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getOwnerOfAcademy(ownerId: Long) = viewModelScope.launch {
        manageRequestUseCases.getOwnerOfAcademy(ownerId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        academyOwner = result.data,
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
                    _eventFlow.emit(UiEvent.ShowToast("정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getAcademyOfClassroom(academyId: Long) = viewModelScope.launch {
        manageRequestUseCases.getAcademyOfClassroom(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        academyInfo = result.data,
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
                    _eventFlow.emit(UiEvent.ShowToast("정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object CloseRemoveDialog: UiEvent()
    }
}