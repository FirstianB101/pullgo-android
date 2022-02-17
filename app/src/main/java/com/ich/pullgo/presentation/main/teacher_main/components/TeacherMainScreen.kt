package com.ich.pullgo.presentation.main.teacher_main.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.R
import com.ich.pullgo.presentation.main.student_main.components.TopBar
import com.ich.pullgo.presentation.main.teacher_main.OwnerCheckViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TeacherMainScreen(
    academyExist: Boolean,
    viewModel: OwnerCheckViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher
    val ownerState = viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.isOwner(teacher?.id!!)
    }

    Surface(color = MaterialTheme.colors.background) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopBar(scope = scope, scaffoldState = scaffoldState, title = "Pullgo") },
            drawerBackgroundColor = colorResource(id = R.color.main_color),
            drawerContent = {
                TeacherMainDrawer(
                    scope = scope,
                    isOwner = ownerState.value,
                    scaffoldState = scaffoldState,
                    navController = navController,
                    academyExist = academyExist
                )
            },
        ) {
            TeacherMainNavigation(navController = navController, academyExist = academyExist)
        }
    }
}