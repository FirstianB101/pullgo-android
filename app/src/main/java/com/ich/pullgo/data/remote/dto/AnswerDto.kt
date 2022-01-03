package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Answer
import java.io.Serializable

data class AnswerDto(
    var answer: List<Int>
): Serializable

fun AnswerDto.toAnswer(): Answer{
    return Answer(answer)
}