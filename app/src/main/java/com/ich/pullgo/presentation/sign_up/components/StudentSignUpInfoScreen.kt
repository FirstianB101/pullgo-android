package com.ich.pullgo.presentation.sign_up.components

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.common.components.OneButtonDialog
import com.ich.pullgo.common.util.TestTags
import com.ich.pullgo.presentation.login.LoginActivity
import com.ich.pullgo.presentation.sign_up.SignUpScreenEvent
import com.ich.pullgo.presentation.sign_up.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun StudentSignUpInfoScreen(
    viewModel: SignUpViewModel
) {
    val state = viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val dialogState = remember { mutableStateOf(false) }
    var verify by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(scaffoldState) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SignUpViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is SignUpViewModel.UiEvent.SignUpSuccess -> {
                    dialogState.value = true
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(60.dp, 60.dp, 60.dp, 0.dp),
                text = stringResource(R.string.input_person_info),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 60.dp),
                text = stringResource(R.string.comment_input_person_info),
                color = colorResource(android.R.color.holo_orange_dark),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_NAME),
                value = state.value.fullName,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.full_name)) },
                onValueChange = { viewModel.onEvent(SignUpScreenEvent.FullNameInputChanged(it)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(30.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(TestTags.SIGNUP_STUDENT_PHONE),
                    value = state.value.phone,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.main_color)
                    ),
                    label = { Text(stringResource(R.string.comment_input_phone)) },
                    onValueChange = { viewModel.onEvent(SignUpScreenEvent.PhoneInputChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.main_color),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {}
                ) {
                    Text(stringResource(R.string.request_verification))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(30.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(TestTags.SIGNUP_STUDENT_PHONE_VERIFY),
                    value = verify,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.main_color)
                    ),
                    label = { Text(stringResource(R.string.comment_input_verification_num)) },
                    onValueChange = { verify = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.main_color),
                        contentColor = colorResource(R.color.white)
                    ),
                    onClick = {}
                ) {
                    Text(stringResource(R.string.check_verification_num))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_PARENT_PHONE),
                value = state.value.parentPhone,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.parent_phone)) },
                onValueChange = { viewModel.onEvent(SignUpScreenEvent.ParentPhoneInputChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_SCHOOL),
                value = state.value.schoolName,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.school)) },
                onValueChange = { viewModel.onEvent(SignUpScreenEvent.SchoolNameInputChanged(it)) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            MultiToggleButton(
                modifier = Modifier.padding(30.dp, 0.dp),
                currentSelection = state.value.schoolYear,
                toggleStates = listOf("1학년", "2학년", "3학년"),
                onToggleChange = { viewModel.onEvent(SignUpScreenEvent.SchoolYearInputChanged(it)) }
            )

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_SUCCESS_BUTTON),
                text = stringResource(R.string.sign_up_success)
            ) {
                if (isAllStudentInfoFilled(
                        fullName = state.value.fullName,
                        phone = state.value.phone,
                        verify = verify,
                        parentPhone = state.value.parentPhone,
                        schoolName = state.value.schoolName
                    )
                ) {
                    viewModel.onEvent(SignUpScreenEvent.CreateStudent)
                } else {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("정보를 모두 입력해 주세요")
                    }
                }
            }

            OneButtonDialog(
                modifier = Modifier.testTag(TestTags.SIGNUP_STUDENT_SUCCESS_DIALOG),
                title = stringResource(R.string.sign_up_success),
                content = stringResource(R.string.comment_sign_up_success),
                buttonText = stringResource(R.string.go_to_login),
                dialogState = dialogState,
                onButtonClick = {
                    dialogState.value = false
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    context.startActivity(intent)
                }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.value.isLoading) {
            CircularProgressIndicator()
        }
    }
}

fun isAllStudentInfoFilled(
    fullName: String,
    phone: String,
    verify: String,
    parentPhone: String,
    schoolName: String
): Boolean {
    return fullName.isNotBlank() && phone.isNotBlank() && verify.isNotBlank() && parentPhone.isNotBlank() && schoolName.isNotBlank()
}