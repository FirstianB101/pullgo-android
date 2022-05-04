package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

sealed class ChangeInfoEvent {
    data class CheckPassword(val inputPassword: String): ChangeInfoEvent()
    object ChangeStudentInfo: ChangeInfoEvent()
    object ChangeTeacherInfo: ChangeInfoEvent()
    data class FullNameChanged(val fullName: String): ChangeInfoEvent()
    data class PhoneChanged(val phone: String): ChangeInfoEvent()
    data class VerifyChanged(val verify: String): ChangeInfoEvent()
    data class ParentPhoneChanged(val parentPhone: String): ChangeInfoEvent()
    data class SchoolNameChanged(val school: String): ChangeInfoEvent()
    data class SchoolYearChanged(val schoolYear: String): ChangeInfoEvent()
}