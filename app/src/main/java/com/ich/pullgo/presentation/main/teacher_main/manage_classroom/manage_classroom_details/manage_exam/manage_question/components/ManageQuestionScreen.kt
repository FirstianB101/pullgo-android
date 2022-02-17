package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import android.net.Uri
import android.provider.MediaStore
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.util.ImageUtil
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun ManageQuestionScreen(
    selectedExam: Exam,
    viewModel: ManageQuestionViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val questions: MutableState<List<Question?>> = remember { mutableStateOf(emptyList()) }

    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()
    val deleteDialogState = remember { mutableStateOf(false) }
    val addQuestionDialogState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getQuestionsInExam(selectedExam.id!!)
    }

    when (state.value) {
        is ManageQuestionState.GetQuestions -> {
            questions.value = (state.value as ManageQuestionState.GetQuestions).questions
            viewModel.onResultConsume()
        }
        is ManageQuestionState.DeleteQuestion -> {
            Toast.makeText(context, "문제가 삭제되었습니다", Toast.LENGTH_SHORT).show()
            viewModel.getQuestionsInExam(selectedExam.id!!)
        }
        is ManageQuestionState.EditQuestion -> {
            Toast.makeText(context, "문제가 수정되었습니다", Toast.LENGTH_SHORT).show()
            viewModel.getQuestionsInExam(selectedExam.id!!)
        }
        is ManageQuestionState.CreateQuestion -> {
            Toast.makeText(context, "문제가 생성되었습니다", Toast.LENGTH_SHORT).show()
            viewModel.getQuestionsInExam(selectedExam.id!!)
        }
        is ManageQuestionState.UploadImage -> {
            questions.value[pagerState.currentPage]?.pictureUrl =
                (state.value as ManageQuestionState.UploadImage).response.data?.url
            viewModel.editQuestion(
                questions.value[pagerState.currentPage]?.id!!,
                questions.value[pagerState.currentPage]!!
            )
        }
        is ManageQuestionState.Loading -> {
            LoadingScreen()
        }
        is ManageQuestionState.Error -> {
            Toast.makeText(context, (state.value as ManageQuestionState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (questions.value.isEmpty()) stringResource(R.string.manage_question)
                        else "문제 ${pagerState.currentPage + 1}"
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (questions.value.isNotEmpty())
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
                            addQuestionDialogState.value = true
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
            if(questions.value.isEmpty()){
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = 1
                ) {
                    NoQuestionScreen()
                }
            }else{
                ModalBottomSheetLayout(
                    sheetState = modalBottomSheetState,
                    sheetContent = {
                        if (questions.value.isNotEmpty()) {
                            EditableMultipleChoiceBottomSheet(
                                choice = questions.value[pagerState.currentPage]?.choice,
                                answer = questions.value[pagerState.currentPage]?.answer!!,
                                onChoiceChanged = { num, choice ->
                                    questions.value[pagerState.currentPage]?.choice?.set(num, choice)
                                },
                                onAnswerChanged = { checks ->
                                    val answers = mutableListOf<Int>()
                                    for (i in checks.indices)
                                        if (checks[i]) answers.add(i + 1)
                                    questions.value[pagerState.currentPage]?.answer = answers
                                }
                            )
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
                            EditableQuestionScreen(
                                modifier = Modifier.fillMaxSize(),
                                question = questions.value[page]!!,
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
                    if (questions.value.isNotEmpty()) {
                        if (questions.value[pagerState.currentPage]?.pictureUrl == null ||
                            questions.value[pagerState.currentPage]?.pictureUrl?.startsWith("http") == true
                        ) {
                            viewModel.editQuestion(
                                questionId = questions.value[pagerState.currentPage]?.id!!,
                                question = questions.value[pagerState.currentPage]!!
                            )
                        } else {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                context.contentResolver,
                                Uri.parse(questions.value[pagerState.currentPage]?.pictureUrl)
                            )
                            val requestBody = ImageUtil.BitmapToString(bitmap)
                                ?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }

                            if (requestBody != null) {
                                viewModel.uploadImage(requestBody)
                            }
                        }
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
            viewModel.deleteQuestion(questions.value[pagerState.currentPage]?.id!!)
            deleteDialogState.value = false
        }
    )

    AddQuestionDialog(
        showDialog = addQuestionDialogState,
        selectedExam = selectedExam,
    ) { newQuestion ->
        viewModel.createQuestion(newQuestion)
        addQuestionDialogState.value = false
    }
}
