package com.ich.pullgo.presentation.main.teacher_main.manage_classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.use_case.manage_classroom.ManageClassroomUseCases
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.utils.WeekdayUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomViewModel @Inject constructor(
    private val manageClassroomUseCases: ManageClassroomUseCases,
    private val app: PullgoApplication
) : ViewModel() {

    private val _state = MutableStateFlow(ManageClassroomState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val teacher by lazy { app.getLoginUser()?.teacher }

    init {
        getClassroomsTeacherApplied(teacher?.id!!)
    }

    fun onEvent(event: ManageClassroomEvent) {
        when (event) {
            is ManageClassroomEvent.SelectClassroom -> {
                _state.value = _state.value.copy(
                    selectedClassroom = event.classroom
                )
            }
            is ManageClassroomEvent.SelectAcademy -> {
                _state.value = _state.value.copy(
                    selectedAcademy = event.academy
                )
            }
            is ManageClassroomEvent.NewClassroomNameChanged -> {
                _state.value = _state.value.copy(
                    newClassroomName = event.name
                )
            }
            is ManageClassroomEvent.NewClassroomWeekdayChanged -> {
                _state.value = _state.value.copy(
                    newClassroomDays = event.weekday
                )
            }
            is ManageClassroomEvent.GetAppliedAcademies -> {
                getAppliedAcademies(teacher?.id!!)
            }
            is ManageClassroomEvent.ResetClassroomList -> {
                getClassroomsTeacherApplied(teacher?.id!!)
            }
            is ManageClassroomEvent.CreateClassroom -> {
                createClassroom(getNewClassroom())
                    .invokeOnCompletion {
                        getClassroomsTeacherApplied(teacher?.id!!)
                        _state.value = _state.value.copy(
                            newClassroomName = "",
                            newClassroomDays = emptyList()
                        )
                    }
            }
        }
    }

    private fun getClassroomsTeacherApplied(teacherId: Long) = viewModelScope.launch {
        manageClassroomUseCases.getClassroomsTeacherApplied(teacherId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        appliedClassrooms = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("반 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun createClassroom(classroom: Classroom) = viewModelScope.launch {
        manageClassroomUseCases.createClassroom(classroom).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반을 생성했습니다"))
                    _eventFlow.emit(UiEvent.CloseCreateDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("반을 생성하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getAppliedAcademies(teacherId: Long) = viewModelScope.launch {
        manageClassroomUseCases.getAppliedAcademies(teacherId).collect { result ->
            when (result) {
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
                    _eventFlow.emit(UiEvent.ShowToast("가입된 학원 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getNewClassroom(): Classroom {
        val selectedDays = state.value.newClassroomDays
        val days = WeekdayUtil.weekdaysToString(selectedDays)

        return Classroom(
            academyId = state.value.selectedAcademy?.id!!,
            name = "${state.value.newClassroomName};$days",
            creatorId = teacher?.id!!
        )
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object CloseCreateDialog : UiEvent()
    }
}