package com.ich.pullgo.presentation.main.common.components.calendar_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.domain.use_case.calendar.CalendarWithLessonUseCases
import com.ich.pullgo.presentation.main.common.components.calendar_screen.util.toDayString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarUseCase: CalendarWithLessonUseCases,
    private val app: PullgoApplication
):ViewModel() {

    private val _state = MutableStateFlow(CalendarLessonState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val currentUser by lazy {app.getLoginUser()}

    init {
        currentUser?.doJob(
            ifStudent = {getStudentLessonsOnMonth(currentUser?.student?.id!!)},
            ifTeacher = {getTeacherLessonsOnMonth(currentUser?.teacher?.id!!)}
        )
    }

    fun onEvent(event: CalendarEvent){
        when(event){
            is CalendarEvent.OnCalendarDaySelected -> {
                _state.value = _state.value.copy(
                    selectedDate = event.day
                )

                state.value.selectedDate?.toDayString()?.let { date ->
                    currentUser?.doJob(
                        ifStudent = { getStudentLessonsOnDate(currentUser?.student?.id!!, date) },
                        ifTeacher = { getTeacherLessonsOnDate(currentUser?.teacher?.id!!, date) }
                    )
                }
            }
            is CalendarEvent.OnLessonSelected -> {
                _state.value = _state.value.copy(
                    selectedLesson = event.lesson
                )
            }
            is CalendarEvent.GetLessonsOnMonth -> {
                currentUser?.doJob(
                    ifStudent = {getStudentLessonsOnMonth(currentUser?.student?.id!!)},
                    ifTeacher = {getTeacherLessonsOnMonth(currentUser?.teacher?.id!!)}
                )
            }
            is CalendarEvent.GetLessonAcademyClassroomInfo -> {
                getAcademySuchLesson(event.academyId)
                getClassroomSuchLesson(event.classroomId)
            }
            is CalendarEvent.CreateLesson -> {
                state.value.selectedDate?.toDayString()?.let { date ->
                    createLesson(event.lesson).invokeOnCompletion {
                        getTeacherLessonsOnMonth(currentUser?.teacher?.id!!)
                        getTeacherLessonsOnDate(currentUser?.teacher?.id!!, date)
                    }
                }
            }
            is CalendarEvent.DeleteLesson -> {
                state.value.selectedDate?.toDayString()?.let { date ->
                    deleteLesson(event.lessonId).invokeOnCompletion {
                        getTeacherLessonsOnMonth(currentUser?.teacher?.id!!)
                        getTeacherLessonsOnDate(currentUser?.teacher?.id!!, date)
                    }
                }
            }
            is CalendarEvent.PatchLesson -> {
                state.value.selectedDate?.toDayString()?.let { date ->
                    patchLesson(event.lessonId, event.lesson).invokeOnCompletion {
                        getTeacherLessonsOnMonth(currentUser?.teacher?.id!!)
                        getTeacherLessonsOnDate(currentUser?.teacher?.id!!,date)
                    }
                }
            }
            is CalendarEvent.GetClassroomsTeacherApplied -> {
                getClassroomsTeacherApplied(currentUser?.teacher?.id!!)
            }
        }
    }

    private fun getStudentLessonsOnDate(studentId: Long, date: String) = viewModelScope.launch {
        calendarUseCase.getStudentLessonsOnDate(studentId, date).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        lessonsOnDate = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowToast("수업 정보를 받아오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getStudentLessonsOnMonth(studentId: Long) = viewModelScope.launch {
        calendarUseCase.getStudentLessonsOnMonth(studentId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        lessonsOnMonth = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowToast("수업 정보를 받아오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getTeacherLessonsOnDate(teacherId: Long, date: String) = viewModelScope.launch {
        calendarUseCase.getTeacherLessonsOnDate(teacherId, date).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        lessonsOnDate = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowToast("수업 정보를 받아오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getTeacherLessonsOnMonth(teacherId: Long) = viewModelScope.launch {
        calendarUseCase.getTeacherLessonsOnMonth(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        lessonsOnMonth = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowToast("수업 정보를 받아오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getAcademySuchLesson(academyId: Long) = viewModelScope.launch {
        calendarUseCase.getAcademySuchLesson(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        academyInfo = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _eventFlow.emit(UiEvent.ShowToast("학원 정보를 받아오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun getClassroomSuchLesson(classroomId: Long) = viewModelScope.launch {
        calendarUseCase.getClassroomSuchLesson(classroomId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        classroomInfo = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun patchLesson(lessonId: Long, lesson: Lesson) = viewModelScope.launch {
        calendarUseCase.patchLessonInfo(lessonId, lesson).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("수업 정보가 변경되었습니다."))
                    _eventFlow.emit(UiEvent.CopyLesson(result.data!!))
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
                    _eventFlow.emit(UiEvent.ShowToast("수업 정보를 변경하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun deleteLesson(lessonId: Long) = viewModelScope.launch {
        calendarUseCase.deleteLesson(lessonId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("수업이 삭제되었습니다."))
                    _eventFlow.emit(UiEvent.CloseLessonDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("수업을 삭제하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun createLesson(lesson: Lesson) = viewModelScope.launch {
        calendarUseCase.createLesson(lesson).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("수업을 생성했습니다."))
                    _eventFlow.emit(UiEvent.CloseCreateLessonDialog)
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
                    _eventFlow.emit(UiEvent.ShowToast("수업을 생성하지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun getClassroomsTeacherApplied(teacherId: Long) = viewModelScope.launch {
        calendarUseCase.getClassroomsTeacherApplied(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        appliedClassrooms = result.data!!,
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
                    _eventFlow.emit(UiEvent.ShowToast("반 정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        data class CopyLesson(val lesson: Lesson): UiEvent()
        object CloseLessonDialog: UiEvent()
        object CloseCreateLessonDialog: UiEvent()
    }
}