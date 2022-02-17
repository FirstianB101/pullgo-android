package com.ich.pullgo.presentation.main.teacher_main.change_info_screen.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoViewModel
import com.ich.pullgo.presentation.sign_up.components.isAllTeacherInfoFilled
import kotlinx.coroutines.launch

@Composable
fun TeacherChangeInfoScreen(
    navController: NavController,
    viewModel: ChangeInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher

    var fullName by remember { mutableStateOf(teacher?.account?.fullName.toString()) }
    var phone by remember { mutableStateOf(teacher?.account?.phone.toString()) }
    var verify by remember { mutableStateOf("") }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
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
                    .padding(0.dp, 0.dp, 0.dp, 60.dp),
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
                onValueChange = { fullName = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(30.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = phone,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(R.color.main_color)
                    ),
                    label = { Text(stringResource(R.string.comment_input_phone)) },
                    onValueChange = { phone = it },
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

            Spacer(modifier = Modifier.height(30.dp))

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                text = stringResource(R.string.change_info)
            ) {
                if (isAllTeacherInfoFilled(fullName, phone, verify)) {
                    val editedTeacher = Teacher(
                        Account(
                            username = teacher?.account?.username,
                            fullName = fullName,
                            phone = phone,
                            password = teacher?.account?.password
                        )
                    )
                    editedTeacher.id = teacher?.id

                    viewModel.changeTeacherInfo(editedTeacher)
                } else {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("정보를 모두 입력해 주세요")
                    }
                }
            }
        }
        when{
            state.value.editedTeacher != null -> {
                val editedTeacher = state.value.editedTeacher
                PullgoApplication.instance?.getLoginUser()?.teacher = editedTeacher

                Toast.makeText(context,"정보가 수정되었습니다",Toast.LENGTH_SHORT).show()

                navController.navigateUp()
                viewModel.onResultConsumed()
            }
            state.value.isLoading -> {
                LoadingScreen()
            }
            state.value.error.isNotBlank() -> {
                Toast.makeText(context,state.value.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
}