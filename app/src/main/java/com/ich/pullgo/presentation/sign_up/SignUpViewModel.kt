package com.ich.pullgo.presentation.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCases: SignUpUseCases
): ViewModel() {

    private val _signUpState = mutableStateOf(SignUpState())
    val signUpState: State<SignUpState> = _signUpState

    fun createStudent(student: Student){
        signUpUseCases.createStudent(student).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState(newStudent = result.data)
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState(isLoading = true)
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState(error = result.message ?: "Unexpected Error Occured")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun createTeacher(teacher: Teacher){
        signUpUseCases.createTeacher(teacher).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState(newTeacher = result.data)
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState(isLoading = true)
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState(error = result.message ?: "Unexpected Error Occured")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun checkIdExist(username: String){
        signUpUseCases.checkIdExist(username).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState(exist = result.data)
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState(isLoading = true)
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState(error = result.message ?: "Unexpected Error Occured")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetState(){
        _signUpState.value = SignUpState()
    }
}