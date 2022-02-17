package com.ich.pullgo.domain.model

import java.io.Serializable

class Question(
    var answer: List<Int>?,
    var choice: MutableMap<String,String>?,
    var pictureUrl: String?,
    var content: String?,
    var examId: Long?
): Serializable {
    var id: Long? = null
}

fun Question.copy(question: Question){
    id = question.id
    answer = question.answer
    choice = question.choice
    pictureUrl = question.pictureUrl
    content = question.content
    examId = question.examId
}