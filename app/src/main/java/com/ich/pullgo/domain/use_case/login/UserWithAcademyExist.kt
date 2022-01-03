package com.ich.pullgo.domain.use_case.login

import com.ich.pullgo.domain.model.User

data class UserWithAcademyExist(
    val user: User? = null,
    var academyExist: Boolean? = null
)