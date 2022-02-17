package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.ManageClassroomManageExamUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomManageExamViewModel @Inject constructor(
    private val manageExamUseCases: ManageClassroomManageExamUseCases
): ViewModel() {
    private val _state = MutableStateFlow<ManageClassroomManageExamState>(ManageClassroomManageExamState.Normal)
    val state: StateFlow<ManageClassroomManageExamState> = _state

    fun getExamsInClassroom(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getExamsInClassroom(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getFinishedExams(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getFinishedExams(classroomId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getCancelledExams(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getCancelledExams(classroomId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetExams(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun createExam(exam: Exam) = viewModelScope.launch {
        manageExamUseCases.createExam(exam).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.CreateExam(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun editExam(examId: Long, exam: Exam) = viewModelScope.launch {
        manageExamUseCases.editExam(examId, exam).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.EditExam(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun deleteExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.deleteExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.DeleteExam
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun finishExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.finishExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.FinishExam
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun cancelExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.cancelExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.CancelExam
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getOneExam(examId: Long) = viewModelScope.launch {
        manageExamUseCases.getOneExam(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetOneExam(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getOneStudent(studentId: Long) = viewModelScope.launch {
        manageExamUseCases.getOneStudent(studentId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetOneStudent(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAttenderStates(examId: Long) = viewModelScope.launch {
        manageExamUseCases.getAttenderStates(examId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetAttenderStates(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun getStudentsInClassroom(classroomId: Long) = viewModelScope.launch {
        manageExamUseCases.getStudentsInClassroom(classroomId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = ManageClassroomManageExamState.GetStudentsInClassroom(result.data!!)
                }
                is Resource.Loading -> {
                    _state.value = ManageClassroomManageExamState.Loading
                }
                is Resource.Error -> {
                    _state.value = ManageClassroomManageExamState.Error(result.message.toString())
                }
            }
        }
    }

    fun onResultConsume(){
        _state.tryEmit(ManageClassroomManageExamState.Normal)
    }
}