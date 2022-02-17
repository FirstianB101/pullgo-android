package com.ich.pullgo.data.remote.dto

import com.ich.pullgo.domain.model.Question
import java.io.Serializable

class QuestionDto(
    var id: Long? = null,
    var answer: List<Int>?,
    var choice: MutableMap<String,String>?,
    var pictureUrl: String?,
    var content: String?,
    var examId: Long?
): Serializable

fun QuestionDto.toQuestion(): Question{
    val question = Question(answer, choice, pictureUrl, content, examId)
    question.id = id
    return question
}