package com.ich.pullgo.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.use_case.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserCase: LoginUseCase
): ViewModel() {
    private val _state = mutableStateOf<LoginState>(LoginState.Normal)
    val state: State<LoginState> = _state

    fun requestLogin(account: Account){
        loginUserCase(account).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = LoginState.SignIn(result.data)
                }
                is Resource.Error -> {
                    _state.value = LoginState.Error(result.message)
                }
                is Resource.Loading -> {
                    _state.value = LoginState.Loading
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resetState(){
        _state.value = LoginState.Normal
    }
}