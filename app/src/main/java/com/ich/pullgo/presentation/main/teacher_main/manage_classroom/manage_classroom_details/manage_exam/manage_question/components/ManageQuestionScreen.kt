package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ManageQuestionScreen(
    selectedExam: Exam,
    navController: NavController,
    viewModel: ManageQuestionViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()
    val deleteDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setExamId(selectedExam.id!!)
        viewModel.onEvent(ManageQuestionEvent.GetQuestionsInExam)
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ManageQuestionViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(state.value.questions, pagerState.currentPage) {
        if (state.value.questions.isNotEmpty()) {
            if (state.value.questions.size <= pagerState.currentPage)
                pagerState.scrollToPage(state.value.questions.size - 1)

            viewModel.onEvent(ManageQuestionEvent.SelectQuestion(state.value.questions[pagerState.currentPage]))
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.value.questions.isEmpty()) stringResource(R.string.manage_question)
                        else "문제 ${pagerState.currentPage + 1}"
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.value.questions.isNotEmpty())
                                deleteDialogState.value = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 5.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            navController.navigate(ManageQuestionScreens.CreateQuestion.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Add,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = colorResource(R.color.main_color),
                elevation = AppBarDefaults.TopAppBarElevation,
                contentColor = Color.White
            )
        },
        content = {
            if (state.value.questions.isEmpty()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = 1
                ) {
                    NoQuestionScreen()
                }
            } else {
                ModalBottomSheetLayout(
                    sheetState = modalBottomSheetState,
                    sheetContent = {
                        Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                            if (state.value.questions.isNotEmpty()) {
                                EditableMultipleChoiceBottomSheet(
                                    choice = state.value.choice,
                                    answer = state.value.answer,
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
                        }
                    }
                ) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 65.dp),
                        count = state.value.questions.size,
                        state = pagerState
                    ) { page ->
                        if (state.value.questions.isNotEmpty()) {
                            EditableQuestionScreen(
                                modifier = Modifier.fillMaxSize(),
                                onImageSelected = {
                                    viewModel.onEvent(ManageQuestionEvent.SetPictureUrl(it.toString()))
                                },
                                contentChanged = {
                                    viewModel.onEvent(ManageQuestionEvent.ContentChanged(it))
                                },
                                state = state.value
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp, end = 25.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
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
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
                cutoutShape = CircleShape,
                backgroundColor = colorResource(R.color.main_color),
                elevation = 22.dp
            ) {
                IconButton(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateBefore,
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        if (pagerState.currentPage < state.value.questions.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.NavigateNext,
                        contentDescription = "",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    if (state.value.questions.isNotEmpty()) {
                        viewModel.onEvent(ManageQuestionEvent.EditQuestion)
                    }
                },
                contentColor = Color.White,
                backgroundColor = Color.Black
            ) {
                Icon(imageVector = Icons.Filled.Save, contentDescription = "Save icon")
            }
        }
    )


    TwoButtonDialog(
        title = "문제 삭제",
        content = "${pagerState.currentPage + 1}번 문제를 삭제하시겠습니까?",
        dialogState = deleteDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = "삭제",
        onCancel = { deleteDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageQuestionEvent.DeleteQuestion)
            deleteDialogState.value = false
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.value.isLoading) {
            CircularProgressIndicator()
        }
    }
}
