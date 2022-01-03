package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Academy
import java.io.Serializable

class AcademyDto(
    var id: Long? = null,
    var name: String?,
    var phone: String?,
    var address: String?,
    var ownerId: Long?
) :Serializable

fun AcademyDto.toAcademy(): Academy{
    val academy = Academy(name, phone, address, ownerId)
    academy.id = id
    return academy
}