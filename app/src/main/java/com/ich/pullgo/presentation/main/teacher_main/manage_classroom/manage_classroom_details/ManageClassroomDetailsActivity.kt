package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom.components.EditClassroomScreen
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components.ManageExamScreen
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_people.components.ManagePeopleScreen
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_request.components.ManageRequestScreen
import dagger.hilt.android.AndroidEntryPoint
import com.ich.pullgo.R
import com.ich.pullgo.presentation.theme.PullgoTheme

@AndroidEntryPoint
class ManageClassroomDetailsActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedClassroom: Classroom = intent.getSerializableExtra("selectedClassroom") as Classroom
        setContent {
            PullgoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val items = listOf(
                        ManageClassroomDetailsScreen.EditClassroom,
                        ManageClassroomDetailsScreen.ManagePeople,
                        ManageClassroomDetailsScreen.ManageRequest,
                        ManageClassroomDetailsScreen.ManageExam,
                    )
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Pullgo",
                                        color = Color.White
                                    )
                                },
                                backgroundColor = colorResource(R.color.main_color)
                            )
                        },
                        bottomBar = {
                            BottomNavigation (
                                backgroundColor = colorResource(R.color.main_color),
                                contentColor = Color.White
                            ){
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    BottomNavigationItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(screen.title) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        alwaysShowLabel = false
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(navController, startDestination = ManageClassroomDetailsScreen.EditClassroom.route, Modifier.padding(innerPadding)) {
                            composable(ManageClassroomDetailsScreen.EditClassroom.route) { EditClassroomScreen(selectedClassroom) }
                            composable(ManageClassroomDetailsScreen.ManagePeople.route) { ManagePeopleScreen(selectedClassroom) }
                            composable(ManageClassroomDetailsScreen.ManageRequest.route) { ManageRequestScreen(selectedClassroom) }
                            composable(ManageClassroomDetailsScreen.ManageExam.route) { ManageExamScreen(selectedClassroom) }
                        }
                    }
                }
            }
        }
    }
}