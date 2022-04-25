package com.ich.pullgo.presentation.login

sealed class LoginScreenEvent {
    object RequestLogin: LoginScreenEvent()
    data class UsernameInputChange(val username: String): LoginScreenEvent()
    data class PasswordInputChange(val password: String): LoginScreenEvent()
}