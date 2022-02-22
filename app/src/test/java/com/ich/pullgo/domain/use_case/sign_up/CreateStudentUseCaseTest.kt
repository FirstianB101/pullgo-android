package com.ich.pullgo.domain.use_case.sign_up

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeSignUpRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateStudentUseCaseTest {

    lateinit var repository: FakeSignUpRepository
    lateinit var useCase: CreateStudentUseCase

    @Before
    fun setUp() {
        repository = FakeSignUpRepository()
        useCase = CreateStudentUseCase(repository)
    }

    @Test
    fun createStudentWithEmptyUsername_returnError() = runBlockingTest {
        val student = Student(
            Account("","fullName","01012341234","password"),
            "01012341234",
            "school",
            1
        )
        val emitted = useCase(student).toList()

        Truth.assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createStudentWithEmptyFullName_returnError() = runBlockingTest {
        val student = Student(
            Account("userName","","01012341234","password"),
            "01012341234",
            "school",
            1
        )
        val emitted = useCase(student).toList()

        Truth.assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createStudent_returnSuccess() = runBlockingTest {
        val student = Student(
            Account("userName","fullName","01012341234","password"),
            "01012341234",
            "school",
            1
        )
        val emitted = useCase(student).toList()

        Truth.assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(emitted[1]).isInstanceOf(Resource.Success::class.java)

        val result = emitted[1].data
        Truth.assertThat(result).isInstanceOf(Student::class.java)
    }
}