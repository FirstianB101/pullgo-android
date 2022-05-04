package com.ich.pullgo.presentation.main.student_main.exam_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.use_case.exam_list.StudentExamListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentExamListViewModel @Inject constructor(
    private val studentExamListUseCases: StudentExamListUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(StudentExamListState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val student by lazy { app.getLoginUser()?.student }

    init {
        onEvent(StudentExamListEvent.GetExamsByName)
    }

    fun onEvent(event: StudentExamListEvent){
        when(event){
            is StudentExamListEvent.SelectExam -> {
                _state.value = _state.value.copy(
                    selectedExam = event.exam
                )
            }
            is StudentExamListEvent.GetExamsByName -> {
                getExamsByName(student?.id!!)
                    .invokeOnCompletion { getStatesByStudentId(student?.id!!)
                        .invokeOnCompletion {
                            _state.value = _state.value.copy(
                                filteredExams = getFilteredExam()
                            )
                        }
                    }
            }
            is StudentExamListEvent.GetExamsByBeginDate -> {
                getExamsByBeginDate(student?.id!!)
                    .invokeOnCompletion { getStatesByStudentId(student?.id!!)
                        .invokeOnCompletion {
                            _state.value = _state.value.copy(
                                filteredExams = getFilteredExam()
                            )
                        }
                    }
            }
            is StudentExamListEvent.GetExamsByEndDate -> {
                getExamsByEndDateDesc(student?.id!!)
                    .invokeOnCompletion { getStatesByStudentId(student?.id!!)
                        .invokeOnCompletion {
                            _state.value = _state.value.copy(
                                filteredExams = getFilteredExam()
                            )
                        }
                    }
            }
            is StudentExamListEvent.StartTakingExam -> {
                startTakingExam(CreateAttender(
                    attenderId = student?.id!!,
                    examId = _state.value.selectedExam?.id!!
                )).invokeOnCompletion { onEvent(StudentExamListEvent.GetExamsByName) }
            }
        }
    }

    private fun getExamsByName(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByName(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        allExams = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getExamsByBeginDate(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByBeginDate(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        allExams = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getExamsByEndDateDesc(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getExamsByEndDateDesc(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        allExams = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getStatesByStudentId(studentId: Long) = viewModelScope.launch {
        studentExamListUseCases.getStatesByStudentId(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        attenderStates = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("응시 정보를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun startTakingExam(attender: CreateAttender) = viewModelScope.launch {
        studentExamListUseCases.startTakingExam(attender).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        examState = result.data!!,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.StartTakingExam(result.data))
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
                    _eventFlow.emit(UiEvent.ShowToast("응시를 시작하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getFilteredExam(): List<Exam> {
        val exceptTaken = mutableListOf<Exam>()
        val checkMap = mutableMapOf<Long,Boolean>()

        for(take in _state.value.attenderStates)
            checkMap[take.examId!!] = true

        for(exam in _state.value.allExams){
            if(exam.finished || exam.cancelled || checkMap[exam.id!!] == true) continue

            exceptTaken.add(exam)
        }
        return exceptTaken
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        data class StartTakingExam(val attenderState: AttenderState): UiEvent()
    }
}