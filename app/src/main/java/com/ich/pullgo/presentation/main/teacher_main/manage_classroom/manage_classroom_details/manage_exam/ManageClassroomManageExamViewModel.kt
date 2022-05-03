package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.ManageClassroomManageExamUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomManageExamViewModel @Inject constructor(
    private val manageExamUseCases: ManageClassroomManageExamUseCases
): ViewModel() {

    private val _state = MutableStateFlow(ManageClassroomManageExamState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var selectedClassroomId: Long = -1L

    fun setClassroomId(classroomId: Long){
        selectedClassroomId = classroomId
    }

    fun onEvent(event: ManageClassroomManageExamEvent){
        when(event){
            is ManageClassroomManageExamEvent.SelectExam -> {
                _state.value = _state.value.copy(
                    selectedExam = event.exam
                )
            }
            is ManageClassroomManageExamEvent.GetAllExams -> {
                getExamsInClassroom(selectedClassroomId)
            }
            is ManageClassroomManageExamEvent.GetFinishedExams -> {
                getFinishedExams(selectedClassroomId)
            }
            is ManageClassroomManageExamEvent.GetCancelledExams -> {
                getCancelledExams(selectedClassroomId)
            }
            is ManageClassroomManageExamEvent.CancelExam -> {
                cancelExam(_state.value.selectedExam?.id!!)
                    .invokeOnCompletion {
                        getExamsInClassroom(selectedClassroomId)
                    }
            }
            is ManageClassroomManageExamEvent.FinishExam -> {
                finishExam(_state.value.selectedExam?.id!!)
                    .invokeOnCompletion {
                        getExamsInClassroom(selectedClassroomId)
                    }
            }
            is ManageClassroomManageExamEvent.DeleteExam -> {
                deleteExam(_state.value.selectedExam?.id!!)
                    .invokeOnCompletion {
                        getExamsInClassroom(selectedClassroomId)
                    }
            }
            is ManageClassroomManageExamEvent.GetStudentStateMap -> {
                getAttenderStates(event.examId)
                    .invokeOnCompletion {
                        val studentStateMap = mutableMapOf<Long,AttenderState>()

                        for(attender in state.value.attenderStates)
                            studentStateMap[attender.attenderId!!] = attender

                        _state.value = _state.value.copy(
                            studentStateMap = studentStateMap
                        )

                        getStudentsInClassroom(selectedClassroomId)
                    }
            }
            is ManageClassroomManageExamEvent.EditExam -> {
                editExam(event.examId,event.exam)
                    .invokeOnCompletion {
                        getExamsInClassroom(selectedClassroomId)
                    }
            }
            is ManageClassroomManageExamEvent.CreateExam -> {
                createExam(event.newExam)
                    .invokeOnCompletion {
                        getExamsInClassroom(selectedClassroomId)
                    }
            }
        }
    }

    private fun getExamsInClassroom(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getExamsInClassroom(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        exams = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getFinishedExams(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getFinishedExams(classroomId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        exams = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getCancelledExams(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getCancelledExams(classroomId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        exams = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험 목록을 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun createExam(exam: Exam) = viewModelScope.launch {
        manageExamUseCases.createExam(exam).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("시험을 생성했습니다."))
                    _eventFlow.emit(UiEvent.RefreshOption)
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 생성하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun editExam(examId: Long, exam: Exam) = viewModelScope.launch {
        manageExamUseCases.editExam(examId, exam).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("시험이 수정됐습니다."))
                    _eventFlow.emit(UiEvent.EditExam(result.data!!))
                    _eventFlow.emit(UiEvent.RefreshOption)
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 수정하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun deleteExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.deleteExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("시험을 삭제했습니다."))
                    _eventFlow.emit(UiEvent.RefreshOption)
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 삭제하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun finishExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.finishExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("시험을 종료했습니다."))
                    _eventFlow.emit(UiEvent.RefreshOption)
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 종료하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun cancelExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.cancelExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("시험을 취소했습니다."))
                    _eventFlow.emit(UiEvent.RefreshOption)
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
                    _eventFlow.emit(UiEvent.ShowToast("시험을 취소하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getOneExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.getOneExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        examInfo = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("시험 정보를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getOneStudent(studentId: Long) = viewModelScope.launch {
        manageExamUseCases.getOneStudent(studentId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        studentInfo = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("학생 정보를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getAttenderStates(examId: Long) = viewModelScope.launch {
        manageExamUseCases.getAttenderStates(examId).collect { result ->
            when (result) {
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

    private fun getStudentsInClassroom(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getStudentsInClassroom(classroomId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        studentsInClassroom = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("학생 정보를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        data class EditExam(val editedExam: Exam): UiEvent()
        object RefreshOption: UiEvent()
    }
}