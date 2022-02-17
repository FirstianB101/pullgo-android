package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.util.ImageUtil
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.ManageQuestionViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddQuestionDialog(
    showDialog: MutableState<Boolean>,
    selectedExam: Exam,
    viewModel: ManageQuestionViewModel = hiltViewModel(),
    onCreateQuestion: (Question) -> Unit,
){
    val state = viewModel.state.collectAsState()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if(showDialog.value) {
        var newQuestion by remember {
            mutableStateOf(Question(listOf(), mutableMapOf(),null,"",null))
        }

        when(state.value){
            is ManageQuestionState.UploadImage -> {
                newQuestion.pictureUrl = (state.value as ManageQuestionState.UploadImage).response.data?.url
                onCreateQuestion(newQuestion)
            }
            is ManageQuestionState.Loading -> {
                LoadingScreen()
            }
        }

        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                showDialog.value = false
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }
        ) {
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
                                choice = mapOf(Pair("1",""),Pair("2",""),Pair("3",""),Pair("4",""),Pair("5","")),
                                answer = listOf(),
                                onChoiceChanged = { num, choice ->
                                    newQuestion.choice?.set(num, choice)
                                },
                                onAnswerChanged = { checks ->
                                    val answers = mutableListOf<Int>()
                                    for(i in checks.indices)
                                        if(checks[i])answers.add(i+1)
                                    newQuestion.answer = answers
                                }
                            )
                        }
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            EditableQuestionScreen(
                                modifier = Modifier.weight(1f),
                                question = newQuestion
                            )
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = colorResource(R.color.secondary_color),
                                    backgroundColor = Color.White
                                ),
                                onClick = {
                                    if(newQuestion.content.isNullOrBlank() || newQuestion.answer.isNullOrEmpty()
                                        || newQuestion.choice.isNullOrEmpty()){
                                        Toast.makeText(context,"입력되지 않은 정보가 있습니다",Toast.LENGTH_SHORT).show()
                                    }else{
                                        newQuestion.examId = selectedExam.id
                                        if(newQuestion.pictureUrl != null) {
                                            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(newQuestion.pictureUrl))
                                            val requestBody = ImageUtil.BitmapToString(bitmap)
                                                    ?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }

                                            if (requestBody != null) {
                                                viewModel.uploadImage(requestBody)
                                            }
                                        }else{
                                            onCreateQuestion(newQuestion)
                                        }
                                    }
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
        }
    }
}