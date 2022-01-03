package com.ich.pullgo.domain.model

class AttenderAnswer(
    var answer: List<Int>,
    var attenderStateId: Long,
    var questionId: Long
) {
    var id: Long? = null
}