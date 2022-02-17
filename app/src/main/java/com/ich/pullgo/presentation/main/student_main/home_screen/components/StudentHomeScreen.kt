package com.ich.pullgo.presentation.main.student_main.home_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ich.pullgo.R
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.presentation.main.common.util.StudentMainScreens

@Composable
fun StudentHomeScreen(
    navController: NavController
){
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = stringResource(R.string.comment_no_applied_academy),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            MainThemeRoundButton(
                text = stringResource(R.string.apply),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp,10.dp)
            ) {
                navController.navigate(StudentMainScreens.ApplyAcademy.route)
            }
        }
    }
}