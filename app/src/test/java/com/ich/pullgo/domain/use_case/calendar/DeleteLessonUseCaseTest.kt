package com.ich.pullgo.domain.use_case.calendar

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeCalendarLessonRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class DeleteLessonUseCaseTest {

    lateinit var repository: FakeCalendarLessonRepository
    lateinit var useCase: DeleteLessonUseCase

    // lesson id already exist
    val LESSON_ID1 = 1L
    val LESSON_ID2 = 2L
    val LESSON_ID3 = 3L
    val LESSON_ID4 = 4L

    @Before
    fun setUp() {
        repository = FakeCalendarLessonRepository()
        useCase = DeleteLessonUseCase(repository)
    }

    @Test
    fun deleteUnAvailableLessonTest_returnHttpException() = runBlockingTest {
        val result = useCase(1L).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun deleteAvailableLessonTest_returnTrue() = runBlockingTest {
        val result = useCase(LESSON_ID1).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        Truth.assertThat(result[1].data).isTrue()
    }
}