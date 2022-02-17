package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.manage_classroom.manage_people.ManageClassroomManagePeopleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomManagePeopleViewModel @Inject constructor(
    private val managePeopleUseCases: ManageClassroomManagePeopleUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageClassroomManagePeopleState>(ManageClassroomManagePeopleState.Normal)
    val state: StateFlow<ManageClassroomManagePeopleState> = _state

    fun getStudentsInClassroom(classroomId: Long) = viewModelScope.launch {
        managePeopleUseCases.getStudents(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManagePeopleState.GetStudents(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManagePeopleState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManagePeopleState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeachersInClassroom(classroomId: Long) = viewModelScope.launch {
        managePeopleUseCases.getTeachers(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManagePeopleState.GetTeachers(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManagePeopleState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManagePeopleState.Error(result.message.toString())
                }
            }
        }
    }

    fun kickStudent(classroomId: Long, studentId: Long) = viewModelScope.launch {
        managePeopleUseCases.kickStudent(classroomId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManagePeopleState.KickPeople
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManagePeopleState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManagePeopleState.Error(result.message.toString())
                }
            }
        }
    }

    fun kickTeacher(classroomId: Long, teacherId: Long) = viewModelScope.launch {
        managePeopleUseCases.kickTeacher(classroomId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManagePeopleState.KickPeople
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManagePeopleState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManagePeopleState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageClassroomManagePeopleState.Normal)
    }
}