package com.ich.pullgo.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.use_case.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginScreenEvent){
        when(event){
            is LoginScreenEvent.UsernameInputChange -> {
                _state.value = _state.value.copy(
                    username = event.username
                )
            }
            is LoginScreenEvent.PasswordInputChange -> {
                _state.value = _state.value.copy(
                    password = event.password
                )
            }
            is LoginScreenEvent.RequestLogin -> {
                requestLogin(
                    Account(
                        username = _state.value.username,
                        password = _state.value.password,
                        fullName = null,
                        phone = null
                    )
                )
            }
        }
    }


    private fun requestLogin(account: Account) = viewModelScope.launch {
        loginUseCase(account).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        userWithAcademyExist = result.data,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.LoginSuccess)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("로그인에 실패했습니다 (${result.message})"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object LoginSuccess: UiEvent()
    }
}