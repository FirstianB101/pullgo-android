package com.ich.pullgo.presentation.main.common.components.apply_academy_screen

import com.ich.pullgo.domain.model.Academy

sealed class ApplyAcademyEvent {
    data class SearchQueryChanged(val query: String): ApplyAcademyEvent()
    data class SelectAcademy(val academy: Academy): ApplyAcademyEvent()
    object CreateAcademy: ApplyAcademyEvent()
    object RequestApplyingAcademy: ApplyAcademyEvent()
    object SearchAcademies: ApplyAcademyEvent()

    data class NewAcademyNameChanged(val name: String): ApplyAcademyEvent()
    data class NewAcademyAddressChanged(val address: String): ApplyAcademyEvent()
    data class NewAcademyPhoneChanged(val phone: String): ApplyAcademyEvent()
}