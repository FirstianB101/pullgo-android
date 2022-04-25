package com.ich.pullgo.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import com.ich.pullgo.presentation.sign_up.util.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCases: SignUpUseCases
): ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Normal)
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun createStudent(student: Student) = viewModelScope.launch{
        signUpUseCases.createStudent(student).collect { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState.CreateStudent(result.data!!)
                    _eventFlow.emit(UiEvent.ShowToast("회원가입에 성공했습니다."))
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.message.toString())
                    _eventFlow.emit(UiEvent.ShowToast("회원가입에 실패했습니다 (${result.message})."))
                }
            }
        }
    }

    fun createTeacher(teacher: Teacher) = viewModelScope.launch{
        signUpUseCases.createTeacher(teacher).collect { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState.CreateTeacher(result.data!!)
                    _eventFlow.emit(UiEvent.ShowToast("회원가입에 성공했습니다."))
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.message.toString())
                    _eventFlow.emit(UiEvent.ShowToast("회원가입에 실패했습니다 (${result.message})."))
                }
            }
        }
    }

    fun checkIdExist(username: String) = viewModelScope.launch{
        signUpUseCases.checkIdExist(username).collect { result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.value = SignUpState.CheckExist(result.data!!)
                    if(result.data){
                        _eventFlow.emit(UiEvent.ShowToast("중복된 아이디입니다."))
                    }
                }
                is Resource.Loading -> {
                    _signUpState.value = SignUpState.Loading
                }
                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.message.toString())
                    _eventFlow.emit(UiEvent.ShowToast("${result.message}"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
    }
}