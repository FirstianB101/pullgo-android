package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ich.pullgo.domain.model.Exam

@Composable
fun ManageQuestionNavigation(
    selectedExam: Exam
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ManageQuestionScreens.ManageQuestion.route
    ) {
        composable(ManageQuestionScreens.ManageQuestion.route) {
            ManageQuestionScreen(selectedExam, navController)
        }
        composable(ManageQuestionScreens.CreateQuestion.route) {
            AddQuestionScreen(selectedExam, navController)
        }
    }
}