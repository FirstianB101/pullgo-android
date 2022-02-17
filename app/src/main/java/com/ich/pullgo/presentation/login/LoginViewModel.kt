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

    private val _state = MutableStateFlow<LoginState>(LoginState.Normal)
    val state: StateFlow<LoginState> = _state

    fun requestLogin(account: Account) = viewModelScope.launch {
        loginUseCase(account).collect { result ->
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
        }
    }

    fun onResultConsumed(){
        _state.tryEmit(LoginState.Normal)
    }
}