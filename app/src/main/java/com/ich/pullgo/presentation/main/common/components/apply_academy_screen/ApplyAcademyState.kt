package com.ich.pullgo.presentation.main.common.components.apply_academy_screen

import com.ich.pullgo.domain.model.Academy

data class ApplyAcademyState(
    val isLoading: Boolean = false,
    val searchedAcademies: List<Academy> = emptyList(),
    val selectedAcademy: Academy? = null,
    val searchQuery: String = "",
    val newAcademyName: String = "",
    val newAcademyAddress: String = "",
    val newAcademyPhone: String = ""
)