package com.ich.pullgo.presentation.main.common.components.apply_academy_screen

import com.ich.pullgo.domain.model.Academy

sealed class ApplyAcademyState {
    object Loading: ApplyAcademyState()
    object Normal: ApplyAcademyState()
    data class Error(val message: String): ApplyAcademyState()
    data class GetSearchedAcademies(val academies: List<Academy>): ApplyAcademyState()
    data class CreateAcademy(val academy: Academy): ApplyAcademyState()
    object RequestApplyAcademy: ApplyAcademyState()
}