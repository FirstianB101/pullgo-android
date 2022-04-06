package com.ich.pullgo.common

object Constants {
    const val BASE_URL = "https://pullgo.firstian.kr/api/v1/"
    const val IMAGE_UPLOAD_URL = "https://api.imgbb.com"
    const val IMAGE_UPLOAD_API_KEY = "b3b9649f31a163c6a3d65ecc7949ca6b"

    const val MAX_LESSONS = 100

    const val HTTP_EXCEPTION_COMMENT = "알 수 없는 오류가 발생했습니다"
    const val CANNOT_CONNECT_SERVER_COMMENT = "서버와 연결할 수 없습니다"
    const val FIRST_WORD_FORMAT_ERROR = "첫 글자는 특수문자를 사용할 수 없습니다"
    const val ID_WRONG_CHAR_ERROR = "영문 소문자, 숫자, 특수문자(- _)만 사용 가능합니다"
    const val ID_TOO_SHORT_ERROR = "아이디가 너무 짧습니다"
    const val ID_TOO_LONG_ERROR = "아이디가 너무 깁니다"
    const val ID_FORMAT_GOOD = "사용 가능한 아이디입니다"

    const val PW_FORMAT_GOOD = "사용 가능한 비밀번호입니다"
    const val PW_TOO_SHORT_ERROR = "비밀번호가 너무 짧습니다"
    const val PW_TOO_LONG_ERROR = "비밀번호가 너무 깁니다"
    const val PW_WRONG_CHAR_ERROR = "사용할 수 없는 문자가 포함되어 있습니다"
    const val PW_CHECK_DIFFERENT_ERROR = "비밀번호를 확인해주세요"

    const val ID_MIN_LENGTH = 8
    const val ID_MAX_LENGTH = 16
    const val PW_MIN_LENGTH = 8
    const val PW_MAX_LENGTH = 16

    const val ID_EXPRESSION = "^[a-z0-9]{1}[a-z0-9-_]*$"
    const val PW_EXPRESSION = "^[\\x00-\\x7F]*$"

    const val LOGIN_IMAGE_LOGO_DESCRIPTION = "Image_Logo"
}