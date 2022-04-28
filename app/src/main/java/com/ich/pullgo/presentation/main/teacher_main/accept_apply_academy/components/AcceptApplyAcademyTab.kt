package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyEvent
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyViewModel

@Composable
fun AcceptApplyAcademyTab(
    viewModel: AcceptApplyAcademyViewModel
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf(stringResource(R.string.student), stringResource(R.string.teacher))

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            contentColor = colorResource(R.color.main_color),
            backgroundColor = Color.White
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(selected = tabIndex == index,
                    onClick = {
                        tabIndex = index

                        if(tabIndex == 0) viewModel.onEvent(AcceptApplyAcademyEvent.GetStudentRequests)
                        else viewModel.onEvent(AcceptApplyAcademyEvent.GetTeacherRequests)
                    },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            softWrap = false,
                            color = Color.Black
                        )
                    }
                )
            }
        }
        when (tabIndex) {
            0 -> {
                AcceptApplyStudentScreen(
                    viewModel = viewModel
                )
            }
            1 -> {
                AcceptApplyTeacherScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}