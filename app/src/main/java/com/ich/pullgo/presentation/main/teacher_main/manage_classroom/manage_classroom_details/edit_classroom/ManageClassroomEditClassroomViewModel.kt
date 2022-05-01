package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.use_case.manage_classroom.edit_classroom.ManageClassroomEditClassroomUseCases
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.utils.WeekdayUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomEditClassroomViewModel @Inject constructor(
    private val editClassroomUseCases: ManageClassroomEditClassroomUseCases
): ViewModel() {
    private val _state = MutableStateFlow(ManageClassroomEditClassroomState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ManageClassroomEditClassroomEvent){
        when(event){
            is ManageClassroomEditClassroomEvent.ClassroomNameChanged -> {
                _state.value = _state.value.copy(
                    classroomName = event.name
                )
            }
            is ManageClassroomEditClassroomEvent.ClassroomDaysChanged -> {
                _state.value = _state.value.copy(
                    classroomDays = event.days
                )
            }
            is ManageClassroomEditClassroomEvent.EditClassroom -> {
                val name = _state.value.classroomName
                val days = WeekdayUtil.weekdaysToString(_state.value.classroomDays)
                editClassroom(
                    classroomId = event.classroomId,
                    classroom = Classroom(null,"$name;$days",null)
                )
            }
            is ManageClassroomEditClassroomEvent.DeleteClassroom -> {
                deleteClassroom(event.classroomId)
            }
        }
    }

    fun editClassroom(classroomId: Long, classroom: Classroom) = viewModelScope.launch {
        editClassroomUseCases.editClassroom(classroomId, classroom).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반 정보가 수정되었습니다"))
                    _eventFlow.emit(UiEvent.EditClassroom(result.data!!))
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
                    _eventFlow.emit(UiEvent.ShowToast("반 정보를 수정하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    fun deleteClassroom(classroomId: Long) = viewModelScope.launch {
        editClassroomUseCases.deleteClassroom(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("반이 삭제되었습니다"))
                    _eventFlow.emit(UiEvent.DeleteClassroom)
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
                    _eventFlow.emit(UiEvent.ShowToast("반을 삭제하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        data class EditClassroom(val editedClassroom: Classroom): UiEvent()
        object DeleteClassroom: UiEvent()
    }
}