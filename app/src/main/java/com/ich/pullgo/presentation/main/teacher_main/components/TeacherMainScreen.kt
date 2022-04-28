package com.ich.pullgo.presentation.main.teacher_main.components

import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.presentation.main.student_main.components.TopBar
import com.ich.pullgo.presentation.main.teacher_main.OwnerCheckViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun TeacherMainScreen(
    academyExist: Boolean,
    viewModel: OwnerCheckViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            if(event is OwnerCheckViewModel.UiEvent.ShowToast){
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
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
                    isOwner = state.value,
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