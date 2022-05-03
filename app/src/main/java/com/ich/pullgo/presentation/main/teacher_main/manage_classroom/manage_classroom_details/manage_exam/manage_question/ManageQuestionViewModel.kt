package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.ImageUtil
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.manage_question.ManageQuestionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ManageQuestionViewModel @Inject constructor(
    private val manageQuestionUseCases: ManageQuestionUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(ManageQuestionState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var examId: Long = -1L

    fun setExamId(examId: Long){
        this.examId = examId
    }

    fun onEvent(event: ManageQuestionEvent){
        when(event){
            is ManageQuestionEvent.SelectQuestion -> {
                _state.value = _state.value.copy(
                    answer = event.question.answer!!,
                    choice = event.question.choice!!,
                    content = event.question.content!!,
                    pictureUrl = event.question.pictureUrl,
                    questionId = event.question.id!!
                )
            }
            is ManageQuestionEvent.AnswerChanged -> {
                _state.value = _state.value.copy(
                    answer = event.answer
                )
            }
            is ManageQuestionEvent.ChoiceChanged -> {
                val map = _state.value.choice.also { it[event.questionNum] = event.choice }
                _state.value = _state.value.copy(
                    choice = map
                )
            }
            is ManageQuestionEvent.ContentChanged -> {
                _state.value = _state.value.copy(
                    content = event.content
                )
            }
            is ManageQuestionEvent.SetPictureUrl -> {
                _state.value = _state.value.copy(
                    pictureUrl = event.url
                )
            }
            is ManageQuestionEvent.CreateQuestion -> {
                if(
                    _state.value.answer.isNullOrEmpty() ||
                    _state.value.choice.isNullOrEmpty() ||
                    _state.value.content.isNullOrEmpty()
                ){
                    _eventFlow.tryEmit(UiEvent.ShowToast("입력되지 않은 정보가 있습니다"))
                }else {
                    if(
                        _state.value.pictureUrl.isNullOrBlank() ||
                        _state.value.pictureUrl?.startsWith("http") == true
                    ){
                        createQuestion(
                            Question(
                                answer = _state.value.answer,
                                choice = _state.value.choice,
                                content = _state.value.content,
                                pictureUrl = _state.value.pictureUrl,
                                examId = examId
                            )
                        ).invokeOnCompletion { getQuestionsInExam(examId) }
                    }else{
                        val requestBody = makeRequestBody()
                        if(requestBody != null){
                            uploadImage(requestBody)
                                .invokeOnCompletion {
                                    createQuestion(
                                        Question(
                                            answer = _state.value.answer,
                                            choice = _state.value.choice,
                                            content = _state.value.content,
                                            pictureUrl = _state.value.pictureUrl,
                                            examId = examId
                                        )
                                    ).invokeOnCompletion { getQuestionsInExam(examId) }
                                }
                        }
                    }
                }
            }
            is ManageQuestionEvent.GetQuestionsInExam -> {
                getQuestionsInExam(examId)
            }
            is ManageQuestionEvent.EditQuestion -> {
                if(
                    _state.value.pictureUrl.isNullOrBlank() ||
                    _state.value.pictureUrl?.startsWith("http") == true
                ) {
                    editQuestion(
                        questionId = _state.value.questionId,
                        question = Question(
                            answer = _state.value.answer,
                            choice = _state.value.choice,
                            content = _state.value.content,
                            pictureUrl = _state.value.pictureUrl,
                            examId = examId
                        )
                    )
                }else{
                    val requestBody = makeRequestBody()
                    if(requestBody != null){
                        uploadImage(requestBody)
                            .invokeOnCompletion {
                                editQuestion(
                                    questionId = _state.value.questionId,
                                    question = Question(
                                        answer = _state.value.answer,
                                        choice = _state.value.choice,
                                        content = _state.value.content,
                                        pictureUrl = _state.value.pictureUrl,
                                        examId = examId
                                    )
                                )
                            }
                    }
                }
            }
            is ManageQuestionEvent.DeleteQuestion -> {
                deleteQuestion(_state.value.questionId)
                    .invokeOnCompletion { getQuestionsInExam(examId) }
            }
        }
    }

    private fun createQuestion(question: Question) = viewModelScope.launch {
        manageQuestionUseCases.createQuestion(question).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제가 생성되었습니다"))
                    _eventFlow.emit(UiEvent.SuccessCreateQuestion)
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제를 생성하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun editQuestion(questionId: Long, question: Question) = viewModelScope.launch {
        manageQuestionUseCases.editQuestion(questionId, question).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제가 수정되었습니다"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제를 수정하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun deleteQuestion(questionId: Long) = viewModelScope.launch {
        manageQuestionUseCases.deleteQuestion(questionId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제가 삭제되었습니다"))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제를 삭제하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getQuestionsInExam(examId: Long) = viewModelScope.launch {
        manageQuestionUseCases.getQuestions(examId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        questions = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("문제를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun uploadImage(image: RequestBody) = viewModelScope.launch {
        manageQuestionUseCases.uploadImage(image).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        pictureUrl = result.data?.data?.url!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("사진을 업로드하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun makeRequestBody(): RequestBody? {
        val bitmap = MediaStore.Images.Media.getBitmap(
            app.contentResolver,
            Uri.parse(_state.value.pictureUrl!!)
        )
        return ImageUtil.BitmapToString(bitmap)
            ?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object SuccessCreateQuestion: UiEvent()
    }
}