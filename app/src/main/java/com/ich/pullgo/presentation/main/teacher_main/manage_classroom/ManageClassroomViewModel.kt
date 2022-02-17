package com.ich.pullgo.presentation.main.teacher_main.manage_classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.use_case.manage_classroom.ManageClassroomUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomViewModel @Inject constructor(
    private val manageClassroomUseCases: ManageClassroomUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageClassroomState>(ManageClassroomState.Normal)
    val state: StateFlow<ManageClassroomState> = _state

    fun getClassroomsTeacherApplied(teacherId: Long) = viewModelScope.launch {
        manageClassroomUseCases.getClassroomsTeacherApplied(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomState.GetClassrooms(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun createClassroom(classroom: Classroom) = viewModelScope.launch {
        manageClassroomUseCases.createClassroom(classroom).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomState.CreateClassroom(classroom)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAppliedAcademies(teacherId: Long) = viewModelScope.launch {
        manageClassroomUseCases.getAppliedAcademies(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomState.GetAcademies(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageClassroomState.Normal)
    }
}