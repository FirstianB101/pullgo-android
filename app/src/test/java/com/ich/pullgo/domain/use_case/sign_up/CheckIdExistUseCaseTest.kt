package com.ich.pullgo.domain.use_case.sign_up

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeSignUpRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.wait
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CheckIdExistUseCaseTest {

    lateinit var repository: FakeSignUpRepository
    lateinit var useCases: SignUpUseCases

    @Before
    fun setUp() {
        repository = FakeSignUpRepository()
        useCases = SignUpUseCases(
            createStudent = CreateStudentUseCase(repository),
            createTeacher = CreateTeacherUseCase(repository),
            checkIdExist = CheckIdExistUseCase(repository)
        )
    }

    @Test
    fun inputIdAlreadyExist_returnTrue() = runBlockingTest {
        // create user for exist test
        useCases.createStudent(
            Student(
                Account("exist_student","fullName","01012341234","password"),
                "01012341234",
                "school",
                1
            )
        ).collect()

        useCases.createTeacher(
            Teacher(
                Account("exist_teacher","fullName","01012341234","password")
            )
        ).collect()

        val exist = useCases.checkIdExist("exist_student").toList()

        Truth.assertThat(exist[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(exist[1]).isInstanceOf(Resource.Success::class.java)

        val result = exist[1].data
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun inputIdNotExist_returnFalse() = runBlockingTest {
        // create user for exist test
        useCases.createStudent(
            Student(
                Account("exist_student","fullName","01012341234","password"),
                "01012341234",
                "school",
                1
            )
        ).collect()

        useCases.createTeacher(
            Teacher(
                Account("exist_teacher","fullName","01012341234","password")
            )
        ).collect()

        val exist = useCases.checkIdExist("not_exist_student").toList()

        Truth.assertThat(exist[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(exist[1]).isInstanceOf(Resource.Success::class.java)

        val result = exist[1].data
        Truth.assertThat(result).isFalse()
    }
}