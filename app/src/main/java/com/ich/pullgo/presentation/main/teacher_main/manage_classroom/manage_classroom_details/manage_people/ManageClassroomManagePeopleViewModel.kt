package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.manage_classroom.manage_people.ManageClassroomManagePeopleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomManagePeopleViewModel @Inject constructor(
    private val managePeopleUseCases: ManageClassroomManagePeopleUseCases
): ViewModel() {

    private val _state = MutableStateFlow(ManageClassroomManagePeopleState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ManageClassroomManagePeopleEvent){
        when(event){
            is ManageClassroomManagePeopleEvent.GetStudentsInClassroom -> {
                getStudentsInClassroom(event.selectedClassroomId)
            }
            is ManageClassroomManagePeopleEvent.GetTeachersInClassroom -> {
                getTeachersInClassroom(event.selectedClassroomId)
            }
            is ManageClassroomManagePeopleEvent.SelectStudent -> {
                _state.value = _state.value.copy(
                    selectedStudent = event.student
                )
            }
            is ManageClassroomManagePeopleEvent.SelectTeacher -> {
                _state.value = _state.value.copy(
                    selectedTeacher = event.teacher
                )
            }
            is ManageClassroomManagePeopleEvent.KickStudent -> {
                kickStudent(event.selectedClassroomId, _state.value.selectedStudent?.id!!)
                    .invokeOnCompletion { getStudentsInClassroom(event.selectedClassroomId) }
            }
            is ManageClassroomManagePeopleEvent.KickTeacher -> {
                kickTeacher(event.selectedClassroomId, _state.value.selectedTeacher?.id!!)
                    .invokeOnCompletion { getTeachersInClassroom(event.selectedClassroomId) }
            }
        }
    }

    private fun getStudentsInClassroom(classroomId: Long) = viewModelScope.launch {
        managePeopleUseCases.getStudents(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        studentsInClassroom = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("학생 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getTeachersInClassroom(classroomId: Long) = viewModelScope.launch {
        managePeopleUseCases.getTeachers(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        teachersInClassroom = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("선생님 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun kickStudent(classroomId: Long, studentId: Long) = viewModelScope.launch {
        managePeopleUseCases.kickStudent(classroomId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학생을 반에서 제외했습니다"))
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

    private fun kickTeacher(classroomId: Long, teacherId: Long) = viewModelScope.launch {
        managePeopleUseCases.kickTeacher(classroomId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("선생님을 반에서 제외했습니다"))
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

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
    }
}