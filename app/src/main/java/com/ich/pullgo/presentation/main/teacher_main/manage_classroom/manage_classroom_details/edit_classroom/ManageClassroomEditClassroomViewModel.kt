package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.use_case.manage_classroom.edit_classroom.ManageClassroomEditClassroomUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomEditClassroomViewModel @Inject constructor(
    private val editClassroomUseCases: ManageClassroomEditClassroomUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageClassroomEditClassroomState>(ManageClassroomEditClassroomState.Normal)
    val state: StateFlow<ManageClassroomEditClassroomState> = _state

    fun editClassroom(classroomId: Long, classroom: Classroom) = viewModelScope.launch {
        editClassroomUseCases.editClassroom(classroomId, classroom).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomEditClassroomState.EditClassroom(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomEditClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomEditClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun deleteClassroom(classroomId: Long) = viewModelScope.launch {
        editClassroomUseCases.deleteClassroom(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomEditClassroomState.DeleteClassroom
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomEditClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomEditClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageClassroomEditClassroomState.Normal)
    }
}