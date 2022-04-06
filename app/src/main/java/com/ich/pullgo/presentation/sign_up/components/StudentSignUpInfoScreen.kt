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
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.common.components.OneButtonDialog
import com.ich.pullgo.common.util.TestTags
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.login.LoginActivity
import com.ich.pullgo.presentation.sign_up.SignUpViewModel
import com.ich.pullgo.presentation.sign_up.util.SignUpState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun StudentSignUpInfoScreen(
    username: String,
    password: String,
    viewModel: SignUpViewModel = hiltViewModel()
){
    val state = viewModel.signUpState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var verify by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var schoolYear by remember { mutableStateOf("1학년") }

    val dialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            if(event is SignUpViewModel.UiEvent.ShowToast){
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when(state.value){
        is SignUpState.CreateStudent -> {
            dialogState.value = true
        }
        is SignUpState.Loading -> {
            LoadingScreen()
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
                    .padding(0.dp,0.dp,0.dp,60.dp),
                text = stringResource(R.string.comment_input_person_info),
                color = colorResource(android.R.color.holo_orange_dark),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_NAME),
                value = fullName,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.full_name)) },
                onValueChange = {fullName = it}
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(30.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .testTag(TestTags.SIGNUP_STUDENT_PHONE),
                    value = phone,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.main_color)
                    ),
                    label = { Text(stringResource(R.string.comment_input_phone)) },
                    onValueChange = {phone = it},
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
                    onValueChange = {verify = it},
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
                value = parentPhone,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.parent_phone)) },
                onValueChange = {parentPhone = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_SCHOOL),
                value = school,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.school)) },
                onValueChange = {school = it},
            )

            Spacer(modifier = Modifier.height(20.dp))

            MultiToggleButton(
                modifier = Modifier.padding(30.dp, 0.dp),
                currentSelection = schoolYear,
                toggleStates = listOf("1학년", "2학년", "3학년"),
                onToggleChange = { schoolYear = it }
            )

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .testTag(TestTags.SIGNUP_STUDENT_SUCCESS_BUTTON),
                text = stringResource(R.string.sign_up_success)
            ) {
                if(isAllStudentInfoFilled(fullName, phone, verify, parentPhone, school)) {
                    val newStudent = Student(
                        Account(
                            username = username,
                            fullName = fullName,
                            phone = phone,
                            password = password
                        ),
                        parentPhone = parentPhone,
                        schoolName = school,
                        schoolYear = when(schoolYear){
                            "1학년" -> 1
                            "2학년" -> 2
                            "3학년" -> 3
                            else -> -1
                        }
                    )
                    viewModel.createStudent(newStudent)
                }else{
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
}

fun isAllStudentInfoFilled(fullName: String, phone: String, verify: String, parentPhone: String, schoolName: String): Boolean{
    return fullName.isNotBlank() && phone.isNotBlank() && verify.isNotBlank() && parentPhone.isNotBlank() && schoolName.isNotBlank()
}