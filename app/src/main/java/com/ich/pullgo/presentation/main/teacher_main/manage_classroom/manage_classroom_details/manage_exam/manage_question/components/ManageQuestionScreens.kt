package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

sealed class ManageQuestionScreens(val route: String) {
    object ManageQuestion: ManageQuestionScreens("manage_question")
    object CreateQuestion: ManageQuestionScreens("create_question")
}
