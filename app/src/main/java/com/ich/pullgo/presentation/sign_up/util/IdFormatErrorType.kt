package com.ich.pullgo.presentation.sign_up.util

import com.ich.pullgo.common.util.Constants

sealed class IdFormatErrorType(val message: String){
    object NoError: IdFormatErrorType(Constants.ID_FORMAT_GOOD)
    object BlankError: IdFormatErrorType("")
    object FirstWordError: IdFormatErrorType(Constants.FIRST_WORD_FORMAT_ERROR)
    object IdWrongCharError: IdFormatErrorType(Constants.ID_WRONG_CHAR_ERROR)
    object IdTooShortError: IdFormatErrorType(Constants.ID_TOO_SHORT_ERROR)
    object IdTooLongError: IdFormatErrorType(Constants.ID_TOO_LONG_ERROR)
}
