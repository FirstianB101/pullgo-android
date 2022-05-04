package com.ich.pullgo.presentation.main.student_main.exam_history_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.use_case.exam_list.StudentExamListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentExamHistoryViewModel @Inject constructor(
    private val studentExamListUseCases: StudentExamListUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(StudentExamHistoryState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val student by lazy { app.getLoginUser()?.student }

    init {
        onEvent(StudentExamHistoryEvent.GetExamsByName)
    }

    fun onEvent(event: StudentExamHistoryEvent){
        when(event){
            is StudentExamHistoryEvent.SelectExam -> {
                _state.value = _state.value.copy(
                    selectedExam = event.exam
                )
            }
            is StudentExamHistoryEvent.GetExamsByName -> {
                getExamsByName(student?.id!!)
                    .invokeOnCompletion { getStatesByStudentId(student?.id!!)
                        .invokeOnCompletion {
                            _state.value = _state.value.copy(
                                takenExams = getTakenExams()
                            )
                        }
                    }
            }
            is StudentExamHistoryEvent.GetExamsByBeginDate -> {
                getExamsByBeginDate(student?.id!!)
                    .invokeOnCompletion { getStatesByStudentId(student?.id!!)
                        .invokeOnCompletion {
                            _state.value = _state.value.copy(
                                takenExams = getTakenExams()
                            )
                        }
                    }
            }
            is StudentExamHistoryEvent.GetExamsByEndDate -> {
                getExamsByEndDateDesc(student?.id!!)
                    .invokeOnCompletion { getStatesByStudentId(student?.id!!)
                        .invokeOnCompletion {
                            _state.value = _state.value.copy(
                                takenExams = getTakenExams()
                            )
                        }
                    }
            }
            is StudentExamHistoryEvent.StartReviewing -> {
                for(attenderState in _state.value.attenderStates){
                    if(_state.value.selectedExam?.id == attenderState.examId){
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.StartReviewing(attenderState))
                        }
                        break
                    }
                }
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

    private fun getTakenExams(): List<Exam>{
        val taken = mutableListOf<Exam>()
        val checkMap = mutableMapOf<Long,Boolean>()

        for(take in _state.value.attenderStates) {
            if(take.progress == Constants.ATTENDER_PROGRESS_COMPLETE) {
                checkMap[take.examId!!] = true
            }
        }

        for(exam in _state.value.allExams){
            if(checkMap[exam.id!!] == true) {
                taken.add(exam)
            }
        }
        return taken
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        data class StartReviewing(val attenderState: AttenderState): UiEvent()
    }
}