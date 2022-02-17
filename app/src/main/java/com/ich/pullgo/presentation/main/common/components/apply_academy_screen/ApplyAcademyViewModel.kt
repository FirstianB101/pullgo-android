package com.ich.pullgo.presentation.main.common.components.apply_academy_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.use_case.apply_academy.ApplyAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyAcademyViewModel @Inject constructor(
    private val applyAcademyUseCases: ApplyAcademyUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ApplyAcademyState>(ApplyAcademyState.Normal)
    val state: StateFlow<ApplyAcademyState> = _state

    fun searchAcademy(query: String) = viewModelScope.launch {
        applyAcademyUseCases.getSearchedAcademies(query).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyAcademyState.GetSearchedAcademies(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun createAcademy(newAcademy: Academy) = viewModelScope.launch {
        applyAcademyUseCases.createAcademy(newAcademy).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyAcademyState.CreateAcademy(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun requestStudentApplyAcademy(studentId: Long, academyId: Long) = viewModelScope.launch {
        applyAcademyUseCases.studentApplyAcademy(studentId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyAcademyState.RequestApplyAcademy
                }
                is Resource.Loading -> {
                    _state.value = ApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun requestTeacherApplyAcademy(teacherId: Long, academyId: Long) = viewModelScope.launch {
        applyAcademyUseCases.teacherApplyAcademy(teacherId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ApplyAcademyState.RequestApplyAcademy
                }
                is Resource.Loading -> {
                    _state.value = ApplyAcademyState.Loading
                }
                is Resource.Error -> {
                    _state.value = ApplyAcademyState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ApplyAcademyState.Normal)
    }
}