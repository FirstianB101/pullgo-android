package com.ich.pullgo.presentation.main.teacher_main.manage_academy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.use_case.manage_academy.ManageAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAcademyViewModel @Inject constructor(
    private val manageAcademyUseCases: ManageAcademyUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageAcademyState>(ManageAcademyState.Normal)
    val state: StateFlow<ManageAcademyState> = _state

    fun getAcademiesTeacherOwned(teacherId: Long) = viewModelScope.launch {
        manageAcademyUseCases.getOwnedAcademies(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.GetAcademies(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun editAcademy(academyId: Long, editedAcademy: Academy) = viewModelScope.launch {
        manageAcademyUseCases.editAcademy(academyId, editedAcademy).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.EditAcademy(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun deleteAcademy(academyId: Long) = viewModelScope.launch {
        manageAcademyUseCases.deleteAcademy(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.DeleteAcademy
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun getStudentsInAcademy(academyId: Long) = viewModelScope.launch {
        manageAcademyUseCases.getStudents(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.GetStudents(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeachersInAcademy(academyId: Long) = viewModelScope.launch {
        manageAcademyUseCases.getTeachers(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.GetTeachers(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun kickTeacher(academyId: Long, teacherId: Long) = viewModelScope.launch {
        manageAcademyUseCases.kickTeacher(academyId, teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.KickUser
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun kickStudent(academyId: Long, studentId: Long) = viewModelScope.launch {
        manageAcademyUseCases.kickStudent(academyId, studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageAcademyState.KickUser
                }
                is Resource.Loading -> {
                    _state.value = ManageAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageAcademyState.Normal)
    }
}