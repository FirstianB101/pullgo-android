package com.ich.pullgo.presentation.main.student_main.change_info_screen.components

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoEvent
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoState
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoViewModel
import com.ich.pullgo.presentation.sign_up.components.MultiToggleButton
import com.ich.pullgo.presentation.sign_up.components.isAllStudentInfoFilled
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun StudentChangeInfoScreen(
    navController: NavController,
    viewModel: ChangeInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var student = PullgoApplication.instance?.getLoginUser()?.student

    var fullName by remember { mutableStateOf(student?.account?.fullName.toString()) }
    var phone by remember { mutableStateOf(student?.account?.phone.toString()) }
    var verify by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf(student?.parentPhone.toString()) }
    var school by remember { mutableStateOf(student?.schoolName.toString()) }
    var schoolYear by remember { mutableStateOf("${student?.schoolYear}학년") }

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ChangeInfoViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ChangeInfoViewModel.UiEvent.SuccessChangingInfo -> {
                    PullgoApplication.instance?.getLoginUser()?.student = state.value.patchedStudent
                    navController.navigateUp()
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
                text = stringResource(R.string.change_person_info),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(0.dp,0.dp,0.dp,60.dp),
                text = stringResource(R.string.comment_change_person_info),
                color = colorResource(android.R.color.holo_orange_dark),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
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
                        .weight(1f),
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
                        .weight(1f),
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
                    .padding(30.dp, 0.dp),
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
                    .padding(30.dp, 0.dp),
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
                toggleStates = listOf("1학년","2학년","3학년"),
                onToggleChange = {schoolYear = it}
            )

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                text = stringResource(R.string.change_info)
            ) {
                if(isAllStudentInfoFilled(fullName, phone, verify, parentPhone, school)) {
                    val editedStudent = Student(
                        Account(
                            username = student?.account?.username,
                            fullName = fullName,
                            phone = phone,
                            password = student?.account?.password
                        ),
                        parentPhone = parentPhone,
                        schoolName = school,
                        schoolYear = when(schoolYear){
                            "1학년" -> 1
                            "2학년" -> 2
                            "3학년" -> 3
                            else -> 0
                        }
                    )
                    editedStudent.id = student?.id

                    viewModel.onEvent(ChangeInfoEvent.ChangeStudentInfo(editedStudent))
                }else{
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("정보를 모두 입력해 주세요")
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}