package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.AttenderState
import java.io.Serializable

class AttenderStateDto(
    var id: Long? = null,
    var attenderId: Long?,
    var examId: Long?,
    var progress: String?,
    var score: Int?
):Serializable

fun AttenderStateDto.toAttenderState(): AttenderState{
    val attenderState = AttenderState(attenderId, examId, progress, score)
    attenderState.id = id
    return attenderState
}

class CreateAttender(
    var attenderId: Long?,
    var examId: Long?
): Serializable