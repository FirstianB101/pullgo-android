package com.ich.pullgo.domain.use_case.calendar

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeCalendarLessonRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetLessonsOnDateUseCaseTest {

    lateinit var repository: FakeCalendarLessonRepository
    // Get Student Lessons / Teacher Lessons are similar in fake repository
    lateinit var useCase: GetStudentLessonsOnDateUseCase

    @Before
    fun setUp() {
        repository = FakeCalendarLessonRepository()
        useCase = GetStudentLessonsOnDateUseCase(repository)
    }

    @Test
    fun getLessonsOnDateWithNoLesson_returnEmptyList() = runBlockingTest {
        // no matched lessons (date) case
        val result = useCase(0L,"1998-09-06").toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        Truth.assertThat(result[1].data).isEmpty()
    }

    @Test
    fun getLessonsOnDateWithTwoLessons_returnLessonsList() = runBlockingTest {
        // two matched lessons (date) case
        val result = useCase(0L,"2022-02-22").toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        Truth.assertThat(result[1].data?.size).isEqualTo(2)
    }
}