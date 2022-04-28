package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.domain.use_case.accept_apply_academy.AcceptApplyAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcceptApplyAcademyViewModel @Inject constructor(
    private val acceptApplyAcademyUseCases: AcceptApplyAcademyUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(AcceptApplyAcademyState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val teacher by lazy { app.getLoginUser()?.teacher }

    init{
        getAppliedAcademies(teacher?.id!!)
    }

    fun onEvent(event: AcceptApplyAcademyEvent){
        when(event){
            is AcceptApplyAcademyEvent.SelectAcademy -> {
                _state.value = _state.value.copy(
                    selectedAcademy = event.academy
                )
                getStudentRequests(_state.value.selectedAcademy?.id!!)
                getTeacherRequests(_state.value.selectedAcademy?.id!!)
            }
            is AcceptApplyAcademyEvent.SelectStudent -> {
                _state.value = _state.value.copy(
                    selectedStudent = event.student
                )
            }
            is AcceptApplyAcademyEvent.SelectTeacher -> {
                _state.value = _state.value.copy(
                    selectedTeacher = event.teacher
                )
            }
            is AcceptApplyAcademyEvent.GetStudentRequests -> {
                getStudentRequests(_state.value.selectedAcademy?.id!!)
            }
            is AcceptApplyAcademyEvent.GetTeacherRequests -> {
                getTeacherRequests(_state.value.selectedAcademy?.id!!)
            }
            is AcceptApplyAcademyEvent.AcceptStudentRequest -> {
                acceptStudentRequest(_state.value.selectedStudent?.id!!, _state.value.selectedAcademy?.id!!)
                    .invokeOnCompletion { getStudentRequests(_state.value.selectedAcademy?.id!!) }
            }
            is AcceptApplyAcademyEvent.AcceptTeacherRequest -> {
                acceptTeacherRequest(_state.value.selectedTeacher?.id!!, _state.value.selectedAcademy?.id!!)
                    .invokeOnCompletion { getTeacherRequests(_state.value.selectedAcademy?.id!!) }
            }
            is AcceptApplyAcademyEvent.DenyStudentRequest -> {
                denyStudentRequest(_state.value.selectedStudent?.id!!, _state.value.selectedAcademy?.id!!)
                    .invokeOnCompletion { getStudentRequests(_state.value.selectedAcademy?.id!!) }
            }
            is AcceptApplyAcademyEvent.DenyTeacherRequest -> {
                denyTeacherRequest(_state.value.selectedTeacher?.id!!, _state.value.selectedAcademy?.id!!)
                    .invokeOnCompletion { getTeacherRequests(_state.value.selectedAcademy?.id!!) }
            }
        }
    }

    private fun getAppliedAcademies(teacherId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.getAcademiesTeacherApplied(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        appliedAcademies = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("학원 정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getStudentRequests(academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.getStudentsApplyingAcademy(academyId).collect { result ->
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
                    _eventFlow.emit(UiEvent.ShowToast("학생 요청 목록을 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getTeacherRequests(academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.getTeachersApplyingAcademy(academyId).collect { result ->
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
                    _eventFlow.emit(UiEvent.ShowToast("선생님 요청 목록을 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun acceptStudentRequest(studentId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.acceptStudentRequest(academyId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학생 가입 요청을 수락하였습니다."))
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
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청을 수락하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun acceptTeacherRequest(teacherId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.acceptTeacherRequest(academyId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("선생님 가입 요청을 수락하였습니다."))
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
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청을 수락하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun denyStudentRequest(studentId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.denyStudentRequest(academyId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학생 가입 요청을 거절하였습니다."))
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
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청을 거절하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun denyTeacherRequest(teacherId: Long, academyId: Long) = viewModelScope.launch {
        acceptApplyAcademyUseCases.denyTeacherRequest(academyId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("선생님 가입 요청을 거절하였습니다."))
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
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청을 거절하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
    }
}