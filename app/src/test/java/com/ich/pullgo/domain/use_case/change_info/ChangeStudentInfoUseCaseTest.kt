package com.ich.pullgo.domain.use_case.change_info

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeChangeInfoRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ChangeStudentInfoUseCaseTest {

    lateinit var repository: FakeChangeInfoRepository
    lateinit var useCase: ChangeStudentInfoUseCase

    val STUDENT_USER_ID = 1L

    @Before
    fun setUp() {
        repository = FakeChangeInfoRepository()
        useCase = ChangeStudentInfoUseCase(repository)
    }

    @Test
    fun patchUnValidStudentInfo_returnError() = runBlockingTest {
        val result = useCase(
            Student(
                Account("patched_student","student","","1234"),
                "",
                "",
                null
            ).also { it.id = 0L }
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun patchStudentInfo_returnSuccess() = runBlockingTest {
        val result = useCase(
            Student(
                Account("patched_student","student","","1234"),
                "",
                "",
                null
            ).also { it.id = STUDENT_USER_ID }
        ).toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val edited = result[1].data
        Truth.assertThat(edited?.account?.username).isEqualTo("patched_student")
    }
}