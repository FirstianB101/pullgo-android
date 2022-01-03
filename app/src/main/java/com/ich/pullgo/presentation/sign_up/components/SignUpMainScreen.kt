package com.ich.pullgo.presentation.sign_up.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ich.pullgo.R
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.presentation.sign_up.SignUpScreen

@Composable
fun SignUpMainScreen(
    navController: NavController
){
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp),
            text = stringResource(R.string.welcome_comment),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        MainThemeRoundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp),
            text = stringResource(R.string.sign_up_student)
        ) {
            navController.navigate(SignUpScreen.StudentIdScreen.route)
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        MainThemeRoundButton(
            modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp, 0.dp),
            text =stringResource(R.string.sign_up_teacher)
        ) {
            navController.navigate(SignUpScreen.TeacherIdScreen.route)
        }
    }
}