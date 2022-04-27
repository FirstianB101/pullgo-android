package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoEvent
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoScreen
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoState
import com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen.ChangeInfoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ChangeInfoCheckPwScreen(
    navController: NavController,
    isTeacher: Boolean,
    viewModel: ChangeInfoViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()

    var pwCheck by rememberSaveable { mutableStateOf("") }
    var pwVisibility by rememberSaveable{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ChangeInfoViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ChangeInfoViewModel.UiEvent.GoToChangeInfoScreen -> {
                    if(isTeacher)
                        navController.navigate(ChangeInfoScreen.TeacherChangeInfoScreen.route)
                    else
                        navController.navigate(ChangeInfoScreen.StudentChangeInfoScreen.route)
                    pwCheck = ""
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                text = stringResource(R.string.comment_input_password_for_edit_person_info),
                color = colorResource(android.R.color.holo_orange_dark),
                fontWeight = FontWeight.Bold
            )


            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 30.dp, 30.dp, 0.dp),
                value = pwCheck,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                visualTransformation = if(pwVisibility) VisualTransformation.None else
                    PasswordVisualTransformation(),
                label = { Text(stringResource(R.string.prompt_password)) },
                trailingIcon = {
                    IconButton(onClick = {
                        pwVisibility = !pwVisibility
                    }) {
                        Icon(
                            imageVector = if (pwVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                onValueChange = {pwCheck = it}
            )

            Spacer(modifier = Modifier.height(36.dp))

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                text = stringResource(R.string.confirm)
            ) {
                viewModel.onEvent(ChangeInfoEvent.CheckPassword(pwCheck))
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