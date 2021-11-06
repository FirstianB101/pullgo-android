package com.harry.pullgo.data.models

import java.io.Serializable

class Question(
    var answer: List<Int>?,
    var choice: Map<String,String>?,
    var pictureUrl: String?,
    var content: String?,
    var examId: Long?
): Serializable {
    var id: Long? = null
}