package com.ich.pullgo.presentation.sign_up.util

import com.ich.pullgo.common.util.Constants

sealed class PwFormatErrorType(val message: String){
    object NoError: PwFormatErrorType(Constants.PW_FORMAT_GOOD)
    object BlankError: PwFormatErrorType("")
    object PwWrongCharError: PwFormatErrorType(Constants.PW_WRONG_CHAR_ERROR)
    object PwTooShortError: PwFormatErrorType(Constants.PW_TOO_SHORT_ERROR)
    object PwTooLongError: PwFormatErrorType(Constants.PW_TOO_LONG_ERROR)
    object PwCheckDifferentError: PwFormatErrorType(Constants.PW_CHECK_DIFFERENT_ERROR)
}