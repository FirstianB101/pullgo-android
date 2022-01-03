package com.ich.pullgo.domain.model

import com.ich.pullgo.data.remote.dto.Data

data class ImageUploadResponse(
    var data: Data?,
    var success: Boolean?,
    var status: Int?
)