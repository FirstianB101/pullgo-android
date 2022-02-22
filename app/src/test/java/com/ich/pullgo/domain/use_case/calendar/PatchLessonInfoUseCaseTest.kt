package com.ich.pullgo.domain.use_case.calendar

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.remote.dto.Schedule
import com.ich.pullgo.data.repository.FakeCalendarLessonRepository
import com.ich.pullgo.domain.model.Lesson
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class PatchLessonInfoUseCaseTest {

    lateinit var repository: FakeCalendarLessonRepository
    lateinit var useCase: PatchLessonInfoUseCase

    // lesson id already exist
    val LESSON_ID1 = 1L
    val LESSON_ID2 = 2L
    val LESSON_ID3 = 3L
    val LESSON_ID4 = 4L

    @Before
    fun setUp() {
        repository = FakeCalendarLessonRepository()
        useCase = PatchLessonInfoUseCase(repository)
    }

    @Test
    fun tryPatchUnAvailableLesson_returnError() = runBlockingTest {
        val result = useCase(
            0L,
            Lesson("patched_lesson",null, Schedule("2022-02-22","10:00:00","11:00:00"),null)
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun patchLesson_returnSuccess() = runBlockingTest {
        val result = useCase(
            LESSON_ID1,
            Lesson("patched_lesson",null,Schedule("2022-02-22","10:00:00","11:00:00"),null)
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val lesson = repository.lessons.find { it.id == LESSON_ID1 }
        Truth.assertThat(lesson?.name).isEqualTo("patched_lesson")
    }
}