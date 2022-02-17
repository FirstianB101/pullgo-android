package com.ich.pullgo.domain.model

import java.io.Serializable

class AttenderState(
    var attenderId: Long?,
    var examId: Long?,
    var progress: String?,
    var score: Int?
):Serializable{
    var id: Long? = null
}