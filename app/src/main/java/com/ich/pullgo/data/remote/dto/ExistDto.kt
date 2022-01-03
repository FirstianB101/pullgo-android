package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Exist

class ExistDto (var exists: Boolean)

fun ExistDto.toExist(): Exist{
    return Exist(exists)
}