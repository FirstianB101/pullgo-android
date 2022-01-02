package com.ich.pullgo.data.models

import java.io.Serializable

class AttenderState(
    var attenderId: Long?,
    var examId: Long?,
    var progress: String?,
    var score: Int?
):Serializable{
    var id: Long? = null
}

class CreateAttender(
    var attenderId: Long?,
    var examId: Long?
): Serializable