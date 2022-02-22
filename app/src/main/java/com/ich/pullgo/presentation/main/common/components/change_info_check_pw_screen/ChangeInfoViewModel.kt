package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.domain.use_case.change_info.ChangeInfoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeInfoViewModel @Inject constructor(
    private val changeInfoUseCases: ChangeInfoUseCases
): ViewModel(){

    private val _state = MutableStateFlow<ChangeInfoState>(ChangeInfoState.Normal)
    val state: StateFlow<ChangeInfoState> = _state

    fun checkPassword(user: User, password: String) = viewModelScope.launch {
        changeInfoUseCases.checkUserPassword(user,password).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ChangeInfoState.AuthUser(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ChangeInfoState.Loading
                }
                is Resource.Error -> {
                    _state.value = ChangeInfoState.Error(result.message.toString())
                }
            }
        }
    }

    fun changeStudentInfo(student: Student) = viewModelScope.launch {
        changeInfoUseCases.changeStudentInfo(student).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ChangeInfoState.PatchStudent(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ChangeInfoState.Loading
                }
                is Resource.Error -> {
                    _state.value = ChangeInfoState.Error(result.message.toString())
                }
            }
        }
    }

    fun changeTeacherInfo(teacher: Teacher) = viewModelScope.launch {
        changeInfoUseCases.changeTeacherInfo(teacher).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ChangeInfoState.PatchTeacher(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ChangeInfoState.Loading
                }
                is Resource.Error -> {
                    _state.value = ChangeInfoState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsumed() {
        _state.tryEmit(ChangeInfoState.Normal)
    }
}