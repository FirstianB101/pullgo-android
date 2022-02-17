package com.ich.pullgo.presentation.main.teacher_main.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.R
import com.ich.pullgo.presentation.main.common.util.TeacherMainScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TeacherMainDrawer(
    academyExist: Boolean,
    isOwner: Boolean,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
){
    val screens_academy_exist = mutableListOf(
        TeacherMainScreens.Calendar,
        TeacherMainScreens.ChangeInfo,
        TeacherMainScreens.ManageClassroom,
        TeacherMainScreens.ApplyClassroom,
        TeacherMainScreens.ApplyAcademy,
        TeacherMainScreens.AcceptApplyingAcademy,
        TeacherMainScreens.ManageRequest
    )

    val screens_no_academy = listOf(
        TeacherMainScreens.HomeNoAcademy,
        TeacherMainScreens.ChangeInfo,
        TeacherMainScreens.ManageRequest
    )

    val activity = LocalContext.current as Activity

    Column(modifier = Modifier.background(Color.White)) {
        val user = PullgoApplication.instance?.getLoginUser()!!

        Row (Modifier.padding(start = 18.dp, top = 36.dp, end = 18.dp)){
            Image(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(R.drawable.plgo_180),
                contentDescription = "App icon"
            )
            Column (modifier = Modifier.padding(10.dp,0.dp)){
                Text(
                    text = user.teacher?.account?.fullName.toString(),
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = user.teacher?.account?.username.toString(),
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Divider()

        Spacer(Modifier.height(24.dp))

        if(isOwner) screens_academy_exist.add(TeacherMainScreens.ManageAcademy)

        val screens = if(academyExist) screens_academy_exist else screens_no_academy

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach{ screen ->
            TeacherDrawerItem(item = screen, selected = currentRoute == screen.route, onItemClick = {
                navController.navigate(screen.route) {
                    launchSingleTop = true
                    restoreState = true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "로그아웃",
            color = Color.Gray,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Start)
                .clickable{
                    PullgoApplication.instance!!.logoutUser()
                    activity.finish()
                }
        )
    }
}