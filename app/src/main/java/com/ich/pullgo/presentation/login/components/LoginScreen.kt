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
import com.ich.pullgo.presentation.login.LoginState
import com.ich.pullgo.presentation.login.LoginViewModel
import com.ich.pullgo.presentation.sign_up.SignUpActivity
import com.ich.pullgo.presentation.student_main.StudentMainActivity
import com.ich.pullgo.presentation.teacher_main.TeacherMainActivity

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    val scaffoldState = rememberScaffoldState()

    var id by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

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
                value = id,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.main_color)
                ),
                label = {Text(stringResource(R.string.prompt_id))},
                onValueChange = {id = it}
            )

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp),
                value = password,
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
                onValueChange = {password = it}
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
                val account = Account(
                    username = id,
                    password = password,
                    fullName = null,
                    phone = null
                )
                viewModel.requestLogin(account)
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

        when(state.value){
            is LoginState.SignIn -> {
                val user = (state.value as LoginState.SignIn).userWithAcademyExist?.user
                val academyExist = (state.value as LoginState.SignIn).userWithAcademyExist?.academyExist

                PullgoApplication.instance?.loginUser(user)
                viewModel.onResultConsumed()
                startMainActivity(context, user, academyExist)
            }
            is LoginState.Loading -> {
                LoadingScreen()
            }
            is LoginState.Error -> {
                Toast.makeText(context, (state.value as LoginState.Error).message,Toast.LENGTH_SHORT).show()
                viewModel.onResultConsumed()
            }
            is LoginState.Normal -> {
                Log.d("LoginActivity","Normal")
            }
        }
    }
}

fun startMainActivity(context: Context, user: User?, academyExist: Boolean?){
    lateinit var intent: Intent

    when{
        user?.teacher != null && academyExist == true -> {
            intent = Intent(context, TeacherMainActivity::class.java)
            intent.putExtra("appliedAcademyExist",true)
        }
        user?.teacher != null && academyExist == false -> {
            intent = Intent(context, TeacherMainActivity::class.java)
        }
        user?.student != null && academyExist == true -> {
            intent = Intent(context, StudentMainActivity::class.java)
            intent.putExtra("appliedAcademyExist",true)
        }
        user?.student != null && academyExist == false -> {
            intent = Intent(context, StudentMainActivity::class.java)
        }
    }

    context.startActivity(intent)
}