package com.ich.pullgo.presentation.login

import com.ich.pullgo.domain.use_case.login.UserWithAcademyExist

data class LoginState(
    val userWithAcademyExist: UserWithAcademyExist? = null,
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)