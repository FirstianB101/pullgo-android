<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/data/models/ImageUploadResponse.kt
package com.ich.pullgo.data.models
=======
package com.ich.pullgo.data.remote.dto
>>>>>>> ich:app/src/main/java/com/ich/pullgo/data/remote/dto/ImageUploadResponseDto.kt

data class ImageUploadResponseDto(
    var data: Data?,
    var success: Boolean?,
    var status: Int?
)

data class Data(
    var id: String?,
    var title: String?,
    var url_viewer: String?,
    var url: String?,
    var display_url: String?,
    var size: String?,
    var time: String?,
    var expiration: String?,
    var image: ImageInfo?,
    var thumb: Thumb?,
    var medium: Medium?,
    var delete_url: String?
)

data class ImageInfo(
    var filename: String?,
    var name: String?,
    var mime: String?,
    var extension: String?,
    var url: String?
)

data class Thumb(
    var filename: String?,
    var name: String?,
    var mime: String?,
    var extension: String?,
    var url: String?
)

class Medium(
    var filename: String?,
    var name: String?,
    var mime: String?,
    var extension: String?,
    var url: String?
)