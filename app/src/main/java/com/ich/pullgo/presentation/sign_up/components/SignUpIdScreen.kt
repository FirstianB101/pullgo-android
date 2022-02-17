package com.ich.pullgo.presentation.sign_up.components

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.common.util.TestTags
import com.ich.pullgo.presentation.sign_up.SignUpViewModel
import com.ich.pullgo.presentation.sign_up.util.IdFormatErrorType
import com.ich.pullgo.presentation.sign_up.util.SignUpState
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
    val state = viewModel.signUpState.collectAsState()
    val context = LocalContext.current

    val targetState = remember{ mutableStateOf(0f) }
    var nextButtonVisible by remember { mutableStateOf(false) }
    val animatedFloatState = animateFloatAsState(
        targetValue = targetState.value,
        animationSpec = tween(durationMillis = 1500)
    )

    when(state.value){
        is SignUpState.CheckExist -> {
            nextButtonVisible = !(state.value as SignUpState.CheckExist).exist
            if(!nextButtonVisible)
                Toast.makeText(context,"중복된 아이디입니다",Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is SignUpState.Error -> {
            Toast.makeText(context, (state.value as SignUpState.Error).message,Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is SignUpState.Loading -> {
            LoadingScreen()
        }
    }

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
                        .padding(30.dp, 30.dp, 30.dp, 0.dp)
                        .testTag(TestTags.SIGNUP_ID_TEXT_FIELD),
                    value = idState.value,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.main_color)
                    ),
                    label = { Text(stringResource(R.string.prompt_id)) },
                    onValueChange = {
                        idState.value = it
                        if(state.value !is SignUpState.CheckExist){
                            viewModel.onResultConsume()
                        }
                    }
                )

                when(checkResult){
                    is IdFormatErrorType.NoError -> {
                        Text(
                            modifier = Modifier.padding(30.dp,0.dp),
                            text = checkResult.message,
                            color = Color.Green
                        )
                    }
                    else -> {
                        Text(
                            modifier = Modifier.padding(30.dp,0.dp),
                            text = checkResult.message,
                            color = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 30.dp, 30.dp, 0.dp)
                    .testTag(TestTags.ID_CHECK_BUTTON),
                text = stringResource(R.string.check_overlap)
            ) {
                when(checkResult){
                    is IdFormatErrorType.NoError -> {
                        viewModel.checkIdExist(idState.value)
                    }
                    else -> {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(message = checkResult.message)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if(nextButtonVisible && checkResult is IdFormatErrorType.NoError){
                targetState.value = 1f
                MainThemeRoundButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp)
                        .alpha(animatedFloatState.value)
                        .testTag(TestTags.SIGNUP_ID_NEXT_BUTTON),
                    text = stringResource(R.string.go_next),
                    onClick = onNextButtonClick
                )
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

    if(!Pattern.matches(Constants.ID_EXPRESSION,inputId)){
        return IdFormatErrorType.IdWrongCharError
    }

    if(Constants.ID_MIN_LENGTH > inputId.length ){
        return IdFormatErrorType.IdTooShortError
    }

    if(inputId.length> Constants.ID_MAX_LENGTH){
        return IdFormatErrorType.IdTooLongError
    }

    return IdFormatErrorType.NoError
}