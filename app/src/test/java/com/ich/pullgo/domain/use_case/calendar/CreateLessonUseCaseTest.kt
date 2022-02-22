package com.ich.pullgo.domain.use_case.calendar

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.remote.dto.Schedule
import com.ich.pullgo.data.repository.FakeCalendarLessonRepository
import com.ich.pullgo.domain.model.Lesson
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CreateLessonUseCaseTest {

    lateinit var repository: FakeCalendarLessonRepository
    lateinit var useCase: CreateLessonUseCase

    @Before
    fun setUp() {
        repository = FakeCalendarLessonRepository()
        useCase = CreateLessonUseCase(repository)
    }

    @Test
    fun createLessonTest_returnNewLesson() = runBlockingTest {
        val result = useCase(
            Lesson("new_lesson",null, Schedule("2022-02-22","10:00:00","12:00:00"),null)
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)
        // check new lesson is same as we requested
        Truth.assertThat(result[1].data?.name).isEqualTo("new_lesson")
    }
}