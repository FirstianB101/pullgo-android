package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.AttenderAnswer

class AttenderAnswerDto(
    var id: Long? = null,
    var answer: List<Int>,
    var attenderStateId: Long,
    var questionId: Long
)

fun AttenderAnswerDto.toAttenderAnswer(): AttenderAnswer{
    val attenderAnswer = AttenderAnswer(answer, attenderStateId, questionId)
    attenderAnswer.id = id
    return attenderAnswer
}