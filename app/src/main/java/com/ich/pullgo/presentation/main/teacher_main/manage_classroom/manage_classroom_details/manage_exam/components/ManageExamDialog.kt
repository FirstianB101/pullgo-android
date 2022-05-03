package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.ManageClassroomManageExamViewModel

@ExperimentalComposeUiApi
@Composable
fun ManageExamDialog(
    selectedExam: Exam,
    viewModel: ManageClassroomManageExamViewModel = hiltViewModel(),
    showDialog: MutableState<Boolean>,
) {
    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            ),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                var tabIndex by remember { mutableStateOf(0) }
                val tabTitles = listOf("시험 정보","응시 현황","시험 관리")

                Column {
                    TabRow(
                        selectedTabIndex = tabIndex,
                        contentColor = colorResource(R.color.main_color),
                        backgroundColor = Color.White
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(selected = tabIndex == index,
                                onClick = { tabIndex = index },
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
                            ManageExamInfoScreen(selectedExam, viewModel)
                        }
                        1 -> {
                            ManageExamStatusScreen(selectedExam, viewModel)
                        }
                        2 -> {
                            ManageExamAndQuestionScreen(selectedExam, viewModel)
                        }
                    }
                }
            }
        }
    }
}