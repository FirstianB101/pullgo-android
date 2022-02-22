package com.ich.pullgo.domain.use_case.sign_up

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeSignUpRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Teacher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CreateTeacherUseCaseTest {

    lateinit var repository: FakeSignUpRepository
    lateinit var useCase: CreateTeacherUseCase

    @Before
    fun setUp() {
        repository = FakeSignUpRepository()
        useCase = CreateTeacherUseCase(repository)
    }

    @Test
    fun createTeacherWithEmptyUsername_returnError() = runBlockingTest {
        val teacher = Teacher(
            Account("","fullName","01012341234","password")
        )
        val emitted = useCase(teacher).toList()

        Truth.assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createTeacherWithEmptyFullName_returnError() = runBlockingTest {
        val teacher = Teacher(
            Account("username","","01012341234","password")
        )
        val emitted = useCase(teacher).toList()

        Truth.assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createTeacher_returnSuccess() = runBlockingTest {
        val teacher = Teacher(
            Account("username","fullName","01012341234","password")
        )
        val emitted = useCase(teacher).toList()

        Truth.assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(emitted[1]).isInstanceOf(Resource.Success::class.java)

        val result = emitted[1].data
        Truth.assertThat(result).isInstanceOf(Teacher::class.java)
    }
}