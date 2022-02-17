package com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.components.AcademyRequestListScreen
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.components.ClassroomRequestListScreen

@ExperimentalComposeUiApi
@Composable
fun ManagePeopleScreen(
    selectedAcademyId: Long
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var tabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf(stringResource(R.string.student), stringResource(R.string.teacher))

        Column {
            TabRow(selectedTabIndex = tabIndex,
                contentColor = colorResource(R.color.main_color),
                backgroundColor = Color.White) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(text = title, maxLines = 1, softWrap = false, color = Color.Black) }
                    )
                }
            }
            when (tabIndex) {
                0 -> {
                    ManageStudentListScreen(selectedAcademyId)
                }
                1 -> {
                    ManageTeacherListScreen(selectedAcademyId)
                }
            }
        }
    }
}