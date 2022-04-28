package com.ich.pullgo.presentation.main.common.components.apply_classroom_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.domain.use_case.apply_classroom.ApplyClassroomUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyClassroomViewModel @Inject constructor(
    private val applyClassroomUseCases: ApplyClassroomUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(ApplyClassroomState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val user by lazy { app.getLoginUser() }

    init {
        user?.doJob(
            ifStudent = { getStudentAppliedAcademies(user?.student?.id!!) },
            ifTeacher = { getTeacherAppliedAcademies(user?.teacher?.id!!) }
        )
    }

    fun onEvent(event: ApplyClassroomEvent){
        when(event){
            is ApplyClassroomEvent.SearchQueryChanged -> {
                _state.value = _state.value.copy(
                    searchQuery = event.query
                )
            }
            is ApplyClassroomEvent.SelectAcademy -> {
                _state.value = _state.value.copy(
                    selectedAcademy = event.academy
                )
            }
            is ApplyClassroomEvent.SelectClassroom -> {
                _state.value = _state.value.copy(
                    selectedClassroom = event.classroom
                )
            }
            is ApplyClassroomEvent.RequestApplyingClassroom -> {
                user?.doJob(
                    ifStudent = { sendStudentApplyClassroomRequest(user?.student?.id!!, _state.value.selectedClassroom?.id!!) },
                    ifTeacher = { sendTeacherApplyClassroomRequest(user?.teacher?.id!!, _state.value.selectedClassroom?.id!!) }
                )
            }
            is ApplyClassroomEvent.SearchClassrooms -> {
                getSearchedClassrooms(
                    academyId = _state.value.selectedAcademy?.id!!,
                    query = _state.value.searchQuery
                )
            }
        }
    }

    private fun getStudentAppliedAcademies(studentId: Long) = viewModelScope.launch {
        applyClassroomUseCases.getStudentAppliedAcademies(studentId).collect { result ->
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
                    _eventFlow.emit(UiEvent.ShowToast("가입된 학원 정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getTeacherAppliedAcademies(teacherId: Long) = viewModelScope.launch {
        applyClassroomUseCases.getTeacherAppliedAcademies(teacherId).collect { result ->
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
                    _eventFlow.emit(UiEvent.ShowToast("가입된 학원 정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getSearchedClassrooms(academyId: Long, query: String) = viewModelScope.launch {
        applyClassroomUseCases.getSearchedClassrooms(academyId, query).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        searchedClassrooms = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("검색 결과를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun sendStudentApplyClassroomRequest(studentId: Long, classroomId: Long) = viewModelScope.launch {
        applyClassroomUseCases.sendStudentApplyClassroomRequest(studentId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반에 가입을 요청했습니다."))
                    _eventFlow.emit(UiEvent.CloseRequestDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청에 실패했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun sendTeacherApplyClassroomRequest(teacherId: Long, classroomId: Long) = viewModelScope.launch {
        applyClassroomUseCases.sendTeacherApplyClassroomRequest(teacherId, classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반에 가입을 요청했습니다."))
                    _eventFlow.emit(UiEvent.CloseRequestDialog)

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
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청에 실패했습니다. (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object CloseRequestDialog: UiEvent()
    }
}