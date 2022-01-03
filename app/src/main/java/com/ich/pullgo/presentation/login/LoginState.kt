package com.ich.pullgo.presentation.login

import com.ich.pullgo.domain.use_case.login.UserWithAcademyExist

sealed class LoginState {
    object Normal : LoginState()
    object Loading : LoginState()
    class Error(val message: String?) : LoginState()
    class SignIn(val userWithAcademyExist: UserWithAcademyExist?) : LoginState()
}
