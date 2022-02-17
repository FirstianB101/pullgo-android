package com.ich.pullgo.presentation.main.common.components.calendar_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.domain.use_case.calendar.CalendarWithLessonUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarUseCase: CalendarWithLessonUseCases
):ViewModel() {
    private val _calendarState = MutableStateFlow<CalendarLessonState>(CalendarLessonState.Normal)
    val calendarState: StateFlow<CalendarLessonState> = _calendarState

    private val _bottomState = MutableStateFlow<CalendarLessonState>(CalendarLessonState.Normal)
    val bottomState: StateFlow<CalendarLessonState> = _bottomState

    private val _dialogState = MutableStateFlow<CalendarLessonState>(CalendarLessonState.Normal)
    val dialogState: StateFlow<CalendarLessonState> = _dialogState

    fun getStudentLessonsOnDate(studentId: Long, date: String) = viewModelScope.launch {
        calendarUseCase.getStudentLessonsOnDate(studentId, date).collect { result ->
            when(result){
                is Resource.Success -> {
                    _bottomState.value = CalendarLessonState.LessonsOnDate(result.data!!)
                }
                is Resource.Loading -> {
                    _bottomState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _bottomState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun getStudentLessonsOnMonth(studentId: Long) = viewModelScope.launch {
        calendarUseCase.getStudentLessonsOnMonth(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _calendarState.value = CalendarLessonState.LessonsOnMonth(result.data!!)
                }
                is Resource.Loading -> {
                    _calendarState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _calendarState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeacherLessonsOnDate(teacherId: Long, date: String) = viewModelScope.launch {
        calendarUseCase.getTeacherLessonsOnDate(teacherId, date).collect { result ->
            when(result){
                is Resource.Success -> {
                    _bottomState.value = CalendarLessonState.LessonsOnDate(result.data!!)
                }
                is Resource.Loading -> {
                    _bottomState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _bottomState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun getTeacherLessonsOnMonth(teacherId: Long) = viewModelScope.launch {
        calendarUseCase.getTeacherLessonsOnMonth(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _calendarState.value = CalendarLessonState.LessonsOnMonth(result.data!!)
                }
                is Resource.Loading -> {
                    _calendarState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _calendarState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun getAcademySuchLesson(academyId: Long) = viewModelScope.launch {
        calendarUseCase.getAcademySuchLesson(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _dialogState.value = CalendarLessonState.GetAcademy(result.data!!)
                }
                is Resource.Loading -> {
                    _dialogState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _dialogState.value = CalendarLessonState.Error(result.message.toString())
                }
            }

        }
    }

    fun getClassroomSuchLesson(classroomId: Long) = viewModelScope.launch {
        calendarUseCase.getClassroomSuchLesson(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _dialogState.value = CalendarLessonState.GetClassroom(result.data!!)
                }
                is Resource.Loading -> {
                    _dialogState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _dialogState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun patchLesson(lessonId: Long, lesson: Lesson) = viewModelScope.launch {
        calendarUseCase.patchLessonInfo(lessonId, lesson).collect { result ->
            when(result){
                is Resource.Success -> {
                    _dialogState.value = CalendarLessonState.PatchLesson(result.data!!)
                }
                is Resource.Loading -> {
                    _dialogState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _dialogState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun deleteLesson(lessonId: Long) = viewModelScope.launch {
        calendarUseCase.deleteLesson(lessonId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _dialogState.value = CalendarLessonState.DeleteLesson
                }
                is Resource.Loading -> {
                    _dialogState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _dialogState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun createLesson(lesson: Lesson) = viewModelScope.launch {
        calendarUseCase.createLesson(lesson).collect { result ->
            when(result){
                is Resource.Success -> {
                    _dialogState.value = CalendarLessonState.CreateLesson(result.data!!)
                }
                is Resource.Loading -> {
                    _dialogState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _dialogState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun getClassroomsTeacherApplied(teacherId: Long) = viewModelScope.launch {
        calendarUseCase.getClassroomsTeacherApplied(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _dialogState.value = CalendarLessonState.AppliedClassrooms(result.data!!)
                }
                is Resource.Loading -> {
                    _dialogState.value = CalendarLessonState.Loading
                }
                is Resource.Error -> {
                    _dialogState.value = CalendarLessonState.Error(result.message.toString())
                }
            }
        }
    }

    fun onCalendarResultConsume(){
        _calendarState.tryEmit(CalendarLessonState.Normal)
    }

    fun onBottomResultConsume(){
        _bottomState.tryEmit(CalendarLessonState.Normal)
    }

    fun onDialogResultConsume(){
        _dialogState.tryEmit(CalendarLessonState.Normal)
    }
}