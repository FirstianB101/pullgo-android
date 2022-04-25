package com.ich.pullgo.presentation.sign_up.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.common.util.TestTags
import com.ich.pullgo.presentation.sign_up.SignUpScreenEvent
import com.ich.pullgo.presentation.sign_up.SignUpViewModel
import com.ich.pullgo.presentation.sign_up.util.PwFormatErrorType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun SignUpPwScreen(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    viewModel: SignUpViewModel,
    onNextButtonClick: () -> Unit
){
    val state = viewModel.state.collectAsState()

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var passwordCheckVisibility by rememberSaveable { mutableStateOf(false) }

    var passwordCheck by rememberSaveable{ mutableStateOf("") }

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val checkResult = checkPw(state.value.password, passwordCheck)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(80.dp),
                text = stringResource(R.string.input_password),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_PW_TEXT_FIELD),
                value = state.value.password,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                visualTransformation = if(passwordVisibility) VisualTransformation.None else
                    PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.prompt_password)) },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                onValueChange = {viewModel.onEvent(SignUpScreenEvent.PasswordInputChanged(it))}
            )

            Text(
                modifier = Modifier
                    .padding(30.dp, 0.dp, 0.dp, 0.dp)
                    .align(Alignment.Start),
                text = checkResult.message,
                color = if (checkResult is PwFormatErrorType.NoError) Color.Green
                        else Color.Red
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_PW_CHECK_TEXT_FIELD),
                value = passwordCheck,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                visualTransformation = if(passwordCheckVisibility) VisualTransformation.None else
                    PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.prompt_password_check)) },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordCheckVisibility = !passwordCheckVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordCheckVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                onValueChange = {passwordCheck = it}
            )
            when(checkResult){
                is PwFormatErrorType.PwCheckDifferentError -> {
                    Text(
                        modifier = Modifier
                            .padding(30.dp, 0.dp, 0.dp, 0.dp)
                            .align(Alignment.Start),
                        text = checkResult.message,
                        color = Color.Red
                    )
                }
                is PwFormatErrorType.NoError -> {
                    Text(
                        modifier = Modifier
                            .padding(30.dp, 0.dp, 0.dp, 0.dp)
                            .align(Alignment.Start),
                        text = checkResult.message,
                        color = Color.Green
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(30.dp))

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_PW_NEXT_BUTTON),
                text = stringResource(R.string.go_next)
            ) {
                when(checkResult){
                    is PwFormatErrorType.NoError -> {
                        onNextButtonClick()
                    }
                    else -> {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(checkResult.message)
                        }
                    }
                }
            }
        }
    }
}


fun checkPw(password: String, checkPassword: String): PwFormatErrorType {
    if(password.isBlank()){
        return PwFormatErrorType.BlankError
    }

    if(Constants.ID_MIN_LENGTH > password.length ){
        return PwFormatErrorType.PwTooShortError
    }

    if(password.length> Constants.ID_MAX_LENGTH){
        return PwFormatErrorType.PwTooLongError
    }

    if(!Pattern.matches(Constants.PW_EXPRESSION,password)){
        return PwFormatErrorType.PwWrongCharError
    }

    if(password != checkPassword){
        return PwFormatErrorType.PwCheckDifferentError
    }

    return PwFormatErrorType.NoError
}