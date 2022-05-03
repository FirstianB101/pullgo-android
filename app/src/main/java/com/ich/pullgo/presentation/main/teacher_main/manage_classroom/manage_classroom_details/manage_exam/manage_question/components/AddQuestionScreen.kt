package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddQuestionScreen(
    selectedExam: Exam,
    navController: NavController,
    viewModel: ManageQuestionViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.setExamId(selectedExam.id!!)
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ManageQuestionViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ManageQuestionViewModel.UiEvent.SuccessCreateQuestion -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    backgroundColor = colorResource(android.R.color.holo_orange_dark),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.FormatListNumbered, "")
                }
            }) {
            ModalBottomSheetLayout(
                sheetState = modalBottomSheetState,
                sheetContent = {
                    EditableMultipleChoiceBottomSheet(
                        choice = mapOf(
                            Pair("1", ""),
                            Pair("2", ""),
                            Pair("3", ""),
                            Pair("4", ""),
                            Pair("5", "")
                        ),
                        answer = listOf(),
                        onChoiceChanged = { num, choice ->
                            viewModel.onEvent(ManageQuestionEvent.ChoiceChanged(num, choice))
                        },
                        onAnswerChanged = { checks ->
                            val answers = mutableListOf<Int>()
                            for (i in checks.indices)
                                if (checks[i]) answers.add(i + 1)
                            viewModel.onEvent(ManageQuestionEvent.AnswerChanged(answers))
                        }
                    )
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    EditableQuestionScreen(
                        modifier = Modifier.weight(1f),
                        onImageSelected = {
                            viewModel.onEvent(ManageQuestionEvent.SetPictureUrl(it.toString()))
                        },
                        contentChanged = {
                            viewModel.onEvent(ManageQuestionEvent.ContentChanged(it))
                        },
                        state = state.value
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(R.color.secondary_color),
                            backgroundColor = Color.White
                        ),
                        onClick = {
                            viewModel.onEvent(ManageQuestionEvent.CreateQuestion)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.create_question),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}