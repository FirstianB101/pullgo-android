package com.ich.pullgo.domain.use_case.calendar

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.remote.dto.Schedule
import com.ich.pullgo.data.repository.FakeCalendarLessonRepository
import com.ich.pullgo.domain.model.Lesson
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CalendarUseCaseTest {

    lateinit var repository: FakeCalendarLessonRepository
    lateinit var useCases: CalendarWithLessonUseCases

    val STUDENT_ID = 1L
    val TEACHER_ID = 2L
    val ACADEMY_ID = 3L
    val CLASSROOM_ID1 = 4L
    val CLASSROOM_ID2 = 5L
    val LESSON_ID1 = 6L
    val LESSON_ID2 = 7L
    val LESSON_ID3 = 8L
    val LESSON_ID4 = 9L

    @Before
    fun setUp(){
        repository = FakeCalendarLessonRepository()
        useCases = CalendarWithLessonUseCases(
            getAcademySuchLesson = GetAcademySuchLessonUseCase(repository),
            getClassroomsTeacherApplied = GetTeacherClassroomsUseCase(repository),
            getClassroomSuchLesson = GetClassroomSuchLessonUseCase(repository),
            getStudentLessonsOnDate = GetStudentLessonsOnDateUseCase(repository),
            getStudentLessonsOnMonth = GetStudentLessonsOnMonthUseCase(repository),
            getTeacherLessonsOnDate = GetTeacherLessonsOnDateUseCase(repository),
            getTeacherLessonsOnMonth = GetTeacherLessonsOnMonthUseCase(repository),
            patchLessonInfo = PatchLessonInfoUseCase(repository),
            deleteLesson = DeleteLessonUseCase(repository),
            createLesson = CreateLessonUseCase(repository)
        )
    }


}