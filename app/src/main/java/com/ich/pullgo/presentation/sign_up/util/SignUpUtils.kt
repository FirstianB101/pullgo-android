package com.ich.pullgo.presentation.sign_up.util

object SignUpUtils {
    fun isAllStudentInfoFilled(
        fullName: String,
        phone: String,
        verify: String,
        parentPhone: String,
        schoolName: String
    ): Boolean {
        return fullName.isNotBlank() && phone.isNotBlank() && verify.isNotBlank() && parentPhone.isNotBlank() && schoolName.isNotBlank()
    }

    fun isAllTeacherInfoFilled(fullName: String, phone: String, verify: String): Boolean{
        return fullName.isNotBlank() && phone.isNotBlank() && verify.isNotBlank()
    }
}