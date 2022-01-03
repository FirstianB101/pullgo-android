package com.ich.pullgo.presentation.login.components

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.ich.pullgo.common.Constants
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.presentation.login.LoginState
import com.ich.pullgo.presentation.login.LoginViewModel
import com.ich.pullgo.presentation.sign_up.SignUpActivity
import com.ich.pullgo.ui.findAccount.FindAccountActivity
import com.ich.pullgo.ui.main.StudentMainActivity
import com.ich.pullgo.ui.main.TeacherMainActivity
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var id by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    var autoLoginCheck by rememberSaveable { mutableStateOf(false) }

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
                    focusedBorderColor = colorResource(R.color.main_color),
                    focusedLabelColor = colorResource(R.color.main_color)
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
                    focusedBorderColor = colorResource(R.color.main_color),
                    focusedLabelColor = colorResource(R.color.main_color)
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
                .padding(0.dp, 0.dp, 30.dp, 0.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Checkbox(
                checked = autoLoginCheck,
                onCheckedChange = {autoLoginCheck = it},
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(R.string.auto_login))
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
                    onClick = {
                        context.startActivity(Intent(context,FindAccountActivity::class.java))
                    }
                )
            }
        }

        when(state){
            is LoginState.SignIn -> {
                val user = state.userWithAcademyExist?.user!!
                val academyExist = state.userWithAcademyExist.academyExist

                PullgoApplication.instance?.loginUser(user)
                viewModel.resetState()
                startMainActivity(context, user, academyExist)
            }
            is LoginState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator(modifier = Modifier)
                }
            }
            is LoginState.Error -> {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = state.message.toString()
                    )
                }
                viewModel.resetState()
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