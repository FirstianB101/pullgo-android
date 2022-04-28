package com.ich.pullgo.presentation.main.teacher_main.manage_academy

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher

sealed class ManageAcademyEvent {
    data class SelectAcademy(val academy: Academy): ManageAcademyEvent()
    data class AcademyAddressChanged(val address: String): ManageAcademyEvent()
    data class AcademyPhoneChanged(val phone: String): ManageAcademyEvent()
    object DeleteAcademy: ManageAcademyEvent()
    object EditAcademy: ManageAcademyEvent()
}