package com.ich.pullgo.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import com.ich.pullgo.presentation.sign_up.util.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCases: SignUpUseCases
): ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Normal)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun createStudent(student: Student) = viewModelScope.launch{
        signUpUseCases.createStudent(student).collect { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState.CreateStudent(result.data!!)
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.message.toString())
                }
            }
        }
    }

    fun createTeacher(teacher: Teacher) = viewModelScope.launch{
        signUpUseCases.createTeacher(teacher).collect { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState.CreateTeacher(result.data!!)
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.message.toString())
                }
            }
        }
    }

    fun checkIdExist(username: String) = viewModelScope.launch{
        signUpUseCases.checkIdExist(username).collect { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState.CheckExist(result.data!!)
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _signUpState.tryEmit(SignUpState.Normal)
    }
}