package com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam.components

import android.app.Activity
import android.os.CountDownTimer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.CenterTopAppBar
import com.ich.pullgo.domain.model.*
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.util.DurationUtil
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review.components.UnEditableQuestionScreen
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam.TakeExamState
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.take_exam.TakeExamViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun TakeExamMainScreen(
    selectedExam: Exam,
    selectedState: AttenderState,
    viewModel: TakeExamViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val context = LocalContext.current

    val questions: MutableState<List<Question>> = remember{ mutableStateOf(emptyList())}
    val answers: MutableState<Array<List<Boolean>>> = remember { mutableStateOf(arrayOf(emptyList())) }

    val finishDialogState = remember { mutableStateOf(false) }
    var timerText by remember{ mutableStateOf("") }

    lateinit var timer: CountDownTimer

    fun saveCurrentAnswer(){
        val checkedAnswer = mutableListOf<Int>()
        for (i in 0..4) {
            if (answers.value[pagerState.currentPage][i])
                checkedAnswer.add(i + 1)
        }

        viewModel.saveAttenderAnswer(
            answer = Answer(checkedAnswer),
            attenderStateId = selectedState.id!!,
            questionId = questions.value[pagerState.currentPage].id!!
        )
    }

    fun startCountDown(startTime: Long){
        timer = object: CountDownTimer(startTime - 1000,1000){
            override fun onTick(millis: Long) {
                val hour = TimeUnit.MILLISECONDS.toHours(millis)
                val minutes = (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millis)))
                val seconds = (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millis)))

                timerText = String.format("%02d:%02d:%02d",hour,minutes,seconds)
            }

            override fun onFinish() {
                viewModel.submitAttenderState(selectedState.id!!)
            }
        }.start()
    }

    LaunchedEffect(Unit){
        viewModel.getQuestionsInExam(selectedExam.id!!)
        startCountDown(DurationUtil.translateDurToMillis(selectedExam.timeLimit!!))
    }

    when(state.value){
        is TakeExamState.GetAnAttenderState -> {
        }
        is TakeExamState.GetQuestions -> {
            questions.value = (state.value as TakeExamState.GetQuestions).questions
            answers.value = Array(questions.value.size){ emptyList() }
            viewModel.onResultConsume()
        }
        is TakeExamState.GetAttenderAnswers -> {
        }
        is TakeExamState.SaveAttenderAnswer -> {
            Toast.makeText(context,"${pagerState.currentPage + 1}번 문제를 저장했습니다",Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is TakeExamState.SubmitAttenderState ->{
            Toast.makeText(context,"시험 응시가 완료되었습니다",Toast.LENGTH_SHORT).show()
            (context as Activity).finish()
        }
        is TakeExamState.Loading -> {
            LoadingScreen()
        }
        is TakeExamState.Error -> {
            Toast.makeText(context, (state.value as TakeExamState.Error).message,Toast.LENGTH_SHORT).show()
            (context as Activity).finish()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CenterTopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = timerText,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            finishDialogState.value = true
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
                            CheckableMultipleChoiceBottomSheet(
                                question = questions.value[pagerState.currentPage],
                                onAnswerChanged = {
                                    answers.value[pagerState.currentPage] = it
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
                    count = questions.value.size,
                    state = pagerState
                ) { page ->
                    if (questions.value.isNotEmpty()) {
                        UnEditableQuestionScreen(
                            modifier = Modifier.fillMaxSize(),
                            question = questions.value[page],
                            questionNum = pagerState.currentPage + 1,
                            isCorrect = null
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
                                if(bottomSheetState.isVisible) {
                                    bottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
                                }else{
                                    bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
                            }
                        },
                        backgroundColor = colorResource(android.R.color.holo_orange_dark),
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Filled.FormatListNumbered, "")
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
                            if(answers.value[pagerState.currentPage].isNotEmpty()){
                                saveCurrentAnswer()
                            }
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
                    if(questions.value.isNotEmpty()) {
                        if(answers.value[pagerState.currentPage].isEmpty()){
                            Toast.makeText(context,"답을 하나 이상 체크해주세요",Toast.LENGTH_SHORT).show()
                        }else {
                            saveCurrentAnswer()
                        }
                    }
                },
                contentColor = Color.White,
                backgroundColor = Color.Black
            ) {
                Icon(imageVector = Icons.Filled.Save, contentDescription = "Take Exam Save icon")
            }
        }
    )

    TwoButtonDialog(
        title = "응시 완료",
        content = "시험 응시를 완료하시겠습니까?",
        dialogState = finishDialogState,
        cancelText = "취소",
        confirmText = "완료",
        onCancel = { finishDialogState.value = false },
        onConfirm = {
            viewModel.submitAttenderState(selectedState.id!!)
            finishDialogState.value = false
        }
    )
}