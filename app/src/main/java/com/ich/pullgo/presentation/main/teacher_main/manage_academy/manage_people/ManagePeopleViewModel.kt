package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.manage_academy.ManageAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagePeopleViewModel @Inject constructor(
    private val manageAcademyUseCases: ManageAcademyUseCases
): ViewModel(){

    private val _state = MutableStateFlow(ManagePeopleState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ManagePeopleEvent){
        when(event){
            is ManagePeopleEvent.SelectStudent -> {
                _state.value = _state.value.copy(
                    selectedStudent = event.student
                )
            }
            is ManagePeopleEvent.SelectTeacher -> {
                _state.value = _state.value.copy(
                    selectedTeacher = event.teacher
                )
            }
            is ManagePeopleEvent.GetStudentsInAcademy -> {
                getStudentsInAcademy(event.academyId)
            }
            is ManagePeopleEvent.GetTeachersInAcademy -> {
                getTeachersInAcademy(event.academyId)
            }
            is ManagePeopleEvent.KickStudent -> {
                kickStudent(event.academyId, _state.value.selectedStudent?.id!!)
                    .invokeOnCompletion { getStudentsInAcademy(event.academyId) }
            }
            is ManagePeopleEvent.KickTeacher -> {
                kickTeacher(event.academyId, _state.value.selectedTeacher?.id!!)
                    .invokeOnCompletion { getTeachersInAcademy(event.academyId) }
            }
        }
    }

    private fun getStudentsInAcademy(academyId: Long) = viewModelScope.launch {
        manageAcademyUseCases.getStudents(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        studentsInAcademy = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("학생 정보를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getTeachersInAcademy(academyId: Long) = viewModelScope.launch {
        manageAcademyUseCases.getTeachers(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        teachersInAcademy = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("선생님 정보를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun kickTeacher(academyId: Long, teacherId: Long) = viewModelScope.launch {
        manageAcademyUseCases.kickTeacher(academyId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("선생님을 학원에서 제외했습니다"))
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
                    _eventFlow.emit(UiEvent.ShowToast("선생님을 제외하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun kickStudent(academyId: Long, studentId: Long) = viewModelScope.launch {
        manageAcademyUseCases.kickStudent(academyId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학생을 학원에서 제외했습니다"))
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
                    _eventFlow.emit(UiEvent.ShowToast("학생을 제외하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
    }
}