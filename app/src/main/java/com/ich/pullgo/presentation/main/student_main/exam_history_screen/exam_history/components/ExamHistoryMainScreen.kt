package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history.ExamHistoryState
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history.ExamHistoryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun ExamHistoryMainScreen(
    selectedExam: Exam,
    attenderState: AttenderState,
    viewModel: ExamHistoryViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val context = LocalContext.current

    val questions: MutableState<List<Question>> = remember{ mutableStateOf(emptyList())}
    val attenderAnswersMap = remember { mutableMapOf<Long,AttenderAnswer>()}

    val exitDialogState = remember { mutableStateOf(false) }
    val resultDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getAttenderAnswers(attenderState.id!!)
    }

    when(state.value){
        is ExamHistoryState.GetAttenderAnswers -> {
            (state.value as ExamHistoryState.GetAttenderAnswers).answers.forEach { attenderAnswer ->
                attenderAnswersMap[attenderAnswer.questionId] = attenderAnswer
            }
            viewModel.getQuestionsInExam(selectedExam.id!!)
        }
        is ExamHistoryState.GetQuestions -> {
            questions.value = (state.value as ExamHistoryState.GetQuestions).questions
            viewModel.onResultConsume()
        }
        is ExamHistoryState.Loading -> {
            LoadingScreen()
        }
        is ExamHistoryState.Error -> {
            Toast.makeText(context, (state.value as ExamHistoryState.Error).message,Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedExam.name ?: ""
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            resultDialogState.value = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(end = 5.dp),
                            imageVector = Icons.Filled.Checklist,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            exitDialogState.value = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Filled.Done,
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
            ModalBottomSheetLayout(
                sheetState = bottomSheetState,
                sheetContent = {
                    Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                        if (questions.value.isNotEmpty()) {
                            UnEditableMultipleChoiceBottomSheet(
                                question = questions.value[pagerState.currentPage],
                                attenderAnswer = attenderAnswersMap[questions.value[pagerState.currentPage].id]
                            )
                        }
                    }
                }
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 65.dp),
                    count = questions.value.size,
                    state = pagerState
                ) { page ->
                    if (questions.value.isNotEmpty()) {
                        UnEditableQuestionScreen(
                            modifier = Modifier.fillMaxSize(),
                            question = questions.value[page],
                            questionNum = pagerState.currentPage + 1,
                            isCorrect = isAnswerCorrect(
                                question = questions.value[pagerState.currentPage],
                                attenderAnswer = attenderAnswersMap[questions.value[pagerState.currentPage].id]
                            )
                        )
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
                        if (pagerState.currentPage < questions.value.size - 1) {
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
                    scope.launch {
                        if(bottomSheetState.isVisible){
                            bottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }else {
                            bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    }
                },
                contentColor = Color.White,
                backgroundColor = colorResource(android.R.color.holo_orange_dark)
            ) {
                Icon(imageVector = Icons.Filled.FormatListNumbered, contentDescription = "exam history choice icon")
            }
        }
    )

    TwoButtonDialog(
        title = "나가기",
        content = "오답노트를 종료하시겠습니까?",
        dialogState = exitDialogState,
        cancelText = "취소",
        confirmText = "나가기",
        onCancel = { exitDialogState.value = false },
        onConfirm = {
            exitDialogState.value = false
            (context as Activity).finish()
        }
    )

    ExamHistoryResultDialog(
        showDialog = resultDialogState,
        questions = questions.value,
        answerMap = attenderAnswersMap,
        scoreText = "${attenderState.score} / 100 점"
    )
}

fun isAnswerCorrect(question: Question, attenderAnswer: AttenderAnswer?): Boolean {
    return question.answer?.size == attenderAnswer?.answer?.size &&
            attenderAnswer != null &&
            question.answer!!.containsAll(attenderAnswer.answer)
}