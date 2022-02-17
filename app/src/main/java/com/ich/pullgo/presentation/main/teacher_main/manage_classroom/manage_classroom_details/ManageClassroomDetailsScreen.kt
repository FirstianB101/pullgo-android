package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ManageClassroomDetailsScreen(val route: String, val title: String,val icon: ImageVector){
    object EditClassroom: ManageClassroomDetailsScreen("edit_classroom","반 정보 수정", Icons.Filled.Edit)
    object ManagePeople: ManageClassroomDetailsScreen("manage_people","구성원 관리", Icons.Filled.Person)
    object ManageRequest: ManageClassroomDetailsScreen("manage_request","요청 관리",Icons.Filled.HowToReg)
    object ManageExam: ManageClassroomDetailsScreen("manage_exam","시험 관리",Icons.Filled.Quiz)
}