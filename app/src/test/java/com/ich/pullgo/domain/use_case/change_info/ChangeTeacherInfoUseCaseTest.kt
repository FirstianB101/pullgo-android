package com.ich.pullgo.domain.use_case.change_info

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeChangeInfoRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Teacher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class ChangeTeacherInfoUseCaseTest {

    lateinit var repository: FakeChangeInfoRepository
    lateinit var useCase: ChangeTeacherInfoUseCase

    val TEACHER_USER_ID = 2L

    @Before
    fun setUp() {
        repository = FakeChangeInfoRepository()
        useCase = ChangeTeacherInfoUseCase(repository)
    }

    @Test
    fun patchUnValidTeacherInfo_returnError() = runBlockingTest {
        val result = useCase(
            Teacher(
                Account("patched_teacher","teacher","","1234")
            ).also { it.id = 0L }
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun patchTeacherInfo_returnSuccess() = runBlockingTest {
        val result = useCase(
            Teacher(
                Account("patched_teacher","teacher","","1234")
            ).also { it.id = TEACHER_USER_ID }
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val edited = result[1].data
        Truth.assertThat(edited?.account?.username).isEqualTo("patched_teacher")
    }
}