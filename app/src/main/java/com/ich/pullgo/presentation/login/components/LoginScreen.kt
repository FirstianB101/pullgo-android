package com.ich.pullgo.presentation.login.components

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.presentation.login.LoginScreenEvent
import com.ich.pullgo.presentation.login.LoginState
import com.ich.pullgo.presentation.login.LoginViewModel
import com.ich.pullgo.presentation.main.student_main.StudentMainActivity
import com.ich.pullgo.presentation.main.teacher_main.TeacherMainActivity
import com.ich.pullgo.presentation.sign_up.SignUpActivity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(scaffoldState){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is LoginViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is LoginViewModel.UiEvent.LoginSuccess -> {
                    val info = state.value.userWithAcademyExist!!
                    PullgoApplication.instance?.loginUser(info.user)
                    startMainActivity(context, info.user, info.academyExist)
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.pullgo_icon),
                contentDescription = Constants.LOGIN_IMAGE_LOGO_DESCRIPTION,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                value = state.value.username,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = { Text(stringResource(R.string.prompt_id)) },
                onValueChange = { viewModel.onEvent(LoginScreenEvent.UsernameInputChange(it)) }
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                value = state.value.password,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                visualTransformation = if(passwordVisibility) VisualTransformation.None else
                    PasswordVisualTransformation(),
                label = {Text(stringResource(R.string.prompt_password))},
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "visibility"
                        )
                    }
                },
                onValueChange = { viewModel.onEvent(LoginScreenEvent.PasswordInputChange(it)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 30.dp)
            ) {

            }

            Spacer(modifier = Modifier.height(20.dp))

            MainThemeRoundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                text = stringResource(R.string.login)
            ) {
                viewModel.onEvent(LoginScreenEvent.RequestLogin)
            }

            Spacer(modifier = Modifier.height(50.dp))
            
            Row(modifier = Modifier
                .fillMaxWidth()
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(40.dp,0.dp,0.dp,0.dp),
                    content = { Text(text = stringResource(R.string.sign_up), color = Color.Gray)},
                    contentPadding = PaddingValues(16.dp),
                    onClick = {
                        context.startActivity(Intent(context,SignUpActivity::class.java))
                    }
                )

                Column(modifier = Modifier.weight(1f)){}

                OutlinedButton(
                    modifier = Modifier.padding(0.dp,0.dp,40.dp,0.dp),
                    content = { Text(text = stringResource(R.string.find_username_and_password), color = Color.Gray)},
                    contentPadding = PaddingValues(16.dp),
                    onClick = {}
                )
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

fun startMainActivity(context: Context, user: User?, academyExist: Boolean?){
    lateinit var intent: Intent

    val app = PullgoApplication.instance!!
    when{
        user?.teacher != null && academyExist == true -> {
            intent = Intent(context, TeacherMainActivity::class.java)
            intent.putExtra("appliedAcademyExist",true)
            app.academyExist = true
        }
        user?.teacher != null && academyExist == false -> {
            intent = Intent(context, TeacherMainActivity::class.java)
            app.academyExist = false
        }
        user?.student != null && academyExist == true -> {
            intent = Intent(context, StudentMainActivity::class.java)
            intent.putExtra("appliedAcademyExist",true)
            app.academyExist = true
        }
        user?.student != null && academyExist == false -> {
            intent = Intent(context, StudentMainActivity::class.java)
            app.academyExist = false
        }
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

    context.startActivity(intent)
}