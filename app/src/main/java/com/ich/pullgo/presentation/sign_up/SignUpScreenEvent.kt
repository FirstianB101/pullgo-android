package com.ich.pullgo.presentation.sign_up

sealed class SignUpScreenEvent {
    object CheckIdExist: SignUpScreenEvent()
    object CreateStudent: SignUpScreenEvent()
    object CreateTeacher: SignUpScreenEvent()
    data class UsernameInputChanged(val id: String): SignUpScreenEvent()
    data class PasswordInputChanged(val pw: String): SignUpScreenEvent()
    data class FullNameInputChanged(val fullName: String): SignUpScreenEvent()
    data class PhoneInputChanged(val phone: String): SignUpScreenEvent()
    data class ParentPhoneInputChanged(val parentPhone: String): SignUpScreenEvent()
    data class SchoolNameInputChanged(val schoolName: String): SignUpScreenEvent()
    data class SchoolYearInputChanged(val schoolYear: String): SignUpScreenEvent()
}