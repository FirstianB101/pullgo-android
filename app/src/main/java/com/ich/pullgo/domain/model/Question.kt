package com.ich.pullgo.domain.model

import java.io.Serializable

class Question(
    var answer: List<Int>?,
    var choice: Map<String, String>?,
    var pictureUrl: String?,
    var content: String?,
    var examId: Long?
) : Serializable {
    var id: Long? = null
}