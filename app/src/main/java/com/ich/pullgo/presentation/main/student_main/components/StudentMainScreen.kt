package com.ich.pullgo.presentation.main.student_main.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun StudentMainScreen(
    academyExist: Boolean
) {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colors.background) {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopBar(scope = scope, scaffoldState = scaffoldState, title = "Pullgo") },
            drawerBackgroundColor = colorResource(id = com.ich.pullgo.R.color.main_color),
            drawerContent = {
                StudentMainDrawer(
                    scope = scope,
                    scaffoldState = scaffoldState,
                    navController = navController,
                    academyExist = academyExist
                )
            },
        ) {
            StudentMainNavigation(navController = navController, academyExist = academyExist)
        }
    }
}
