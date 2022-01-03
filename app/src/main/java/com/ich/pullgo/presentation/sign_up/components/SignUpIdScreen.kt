package com.ich.pullgo.presentation.sign_up.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.Constants
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.presentation.sign_up.SignUpScreen
import com.ich.pullgo.presentation.sign_up.SignUpState
import com.ich.pullgo.presentation.sign_up.SignUpViewModel
import com.ich.pullgo.presentation.sign_up.util.IdFormatErrorType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun SignUpIdScreen(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    idState: MutableState<String>,
    viewModel: SignUpViewModel = hiltViewModel(),
    onNextButtonClick: () -> Unit
){
    val state = viewModel.signUpState.value

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(80.dp),
                text = stringResource(R.string.input_id),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            val checkResult = checkId(idState.value)
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 30.dp, 30.dp, 0.dp),
                    value = idState.value,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.main_color),
                        focusedLabelColor = colorResource(R.color.main_color)
                    ),
                    label = { Text(stringResource(R.string.prompt_id)) },
                    onValueChange = {
                        idState.value = it
                        if(state.exist != null){
                            viewModel.resetState()
                        }
                    }
                )


                when(checkResult){
                    is IdFormatErrorType.NoError -> {
                        Text(
                            modifier = Modifier.padding(30.dp,0.dp),
                            text = checkResult.msg,
                            color = Color.Green
                        )
                    }
                    else -> {
                        Text(
                            modifier = Modifier.padding(30.dp,0.dp),
                            text = checkResult.msg,
                            color = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 30.dp, 30.dp, 0.dp),
                text = stringResource(R.string.check_overlap)
            ) {
                when(checkResult){
                    is IdFormatErrorType.NoError -> {
                        viewModel.checkIdExist(idState.value)
                    }
                    else -> {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(message = checkResult.msg)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when{
                isNotExistId(state.exist) && checkResult is IdFormatErrorType.NoError -> {
                    MainThemeRoundButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        text = stringResource(R.string.go_next),
                        onClick = onNextButtonClick
                    )
                }
                state.exist == true -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("중복된 아이디입니다")
                    }
                }
            }
        }
    }
}


fun checkId(inputId: String): IdFormatErrorType{
    if(inputId.isBlank()){
        return IdFormatErrorType.BlankError
    }

    if(inputId[0] == '-' || inputId[0] == '_'){
        return IdFormatErrorType.FirstWordError
    }

    if(Constants.ID_MIN_LENGTH > inputId.length ){
        return IdFormatErrorType.IdTooShortError
    }

    if(inputId.length> Constants.ID_MAX_LENGTH){
        return IdFormatErrorType.IdTooLongError
    }

    if(!Pattern.matches(Constants.ID_EXPRESSION,inputId)){
        return IdFormatErrorType.IdWrongCharError
    }
    return IdFormatErrorType.NoError
}

fun isNotExistId(exist: Boolean?) = (exist != null && exist == false)