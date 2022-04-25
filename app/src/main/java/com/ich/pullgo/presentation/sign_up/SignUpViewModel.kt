package com.ich.pullgo.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import com.ich.pullgo.presentation.sign_up.util.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCases: SignUpUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: SignUpScreenEvent) {
        when (event) {
            is SignUpScreenEvent.UsernameInputChanged -> {
                _state.value = _state.value.copy(
                    username = event.id
                )
            }
            is SignUpScreenEvent.PasswordInputChanged -> {
                _state.value = _state.value.copy(
                    password = event.pw
                )
            }
            is SignUpScreenEvent.FullNameInputChanged -> {
                _state.value = _state.value.copy(
                    fullName = event.fullName
                )
            }
            is SignUpScreenEvent.PhoneInputChanged -> {
                _state.value = _state.value.copy(
                    phone = event.phone
                )
            }
            is SignUpScreenEvent.ParentPhoneInputChanged -> {
                _state.value = _state.value.copy(
                    parentPhone = event.parentPhone
                )
            }
            is SignUpScreenEvent.SchoolNameInputChanged -> {
                _state.value = _state.value.copy(
                    schoolName = event.schoolName
                )
            }
            is SignUpScreenEvent.SchoolYearInputChanged -> {
                _state.value = _state.value.copy(
                    schoolYear = event.schoolYear
                )
            }
            is SignUpScreenEvent.CheckIdExist -> {
                checkIdExist(_state.value.username)
            }
            is SignUpScreenEvent.CreateStudent -> {
                createStudent(
                    Student(
                        Account(
                            username = _state.value.username,
                            fullName = _state.value.fullName,
                            phone = _state.value.phone,
                            password = _state.value.password
                        ),
                        parentPhone = _state.value.parentPhone,
                        schoolName = _state.value.schoolName,
                        schoolYear = when(_state.value.schoolYear){
                            "1학년" -> 1
                            "2학년" -> 2
                            "3학년" -> 3
                            else -> throw Exception("schoolYear selection error")
                        }
                    )
                )
            }
            is SignUpScreenEvent.CreateTeacher -> {
                createTeacher(
                    Teacher(
                        Account(
                            username = _state.value.username,
                            fullName = _state.value.fullName,
                            phone = _state.value.phone,
                            password = _state.value.password
                        ),
                    )
                )
            }
        }
    }

    private fun createStudent(student: Student) = viewModelScope.launch {
        signUpUseCases.createStudent(student).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        createStudent = result.data,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.SignUpSuccess)
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
                    _eventFlow.emit(UiEvent.ShowToast("회원가입에 실패했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun createTeacher(teacher: Teacher) = viewModelScope.launch {
        signUpUseCases.createTeacher(teacher).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        createTeacher = result.data,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.SignUpSuccess)
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
                    _eventFlow.emit(UiEvent.ShowToast("회원가입에 실패했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun checkIdExist(username: String) = viewModelScope.launch {
        signUpUseCases.checkIdExist(username).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        idExist = result.data,
                        isLoading = false
                    )
                    if (result.data == true)
                        _eventFlow.emit(UiEvent.ShowToast("중복된 아이디입니다."))
                    else
                        _eventFlow.emit(UiEvent.ShowNextButton)
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
                    _eventFlow.emit(UiEvent.ShowToast("중복 확인에 실패했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object SignUpSuccess: UiEvent()
        object ShowNextButton: UiEvent()
    }
}