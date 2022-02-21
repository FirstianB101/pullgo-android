package com.ich.pullgo.domain.use_case.sign_up

import com.google.common.truth.Truth.assertThat
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeSignUpRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class SignUpUseCaseTest {

    lateinit var repository: FakeSignUpRepository
    lateinit var useCases: SignUpUseCases

    @Before
    fun setUp(){
        repository = FakeSignUpRepository()
        useCases = SignUpUseCases(
            createStudent = CreateStudentUseCase(repository),
            createTeacher = CreateTeacherUseCase(repository),
            checkIdExist = CheckIdExistUseCase(repository)
        )
    }

    @Test
    fun inputIdAlreadyExist_returnTrue() = runBlockingTest {
        val exist = useCases.checkIdExist("exist_student").toList()

        assertThat(exist[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(exist[1]).isInstanceOf(Resource.Success::class.java)

        val result = exist[1].data
        assertThat(result).isTrue()
    }

    @Test
    fun inputIdNotExist_returnFalse() = runBlockingTest {
        val exist = useCases.checkIdExist("not_exist_student").toList()

        assertThat(exist[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(exist[1]).isInstanceOf(Resource.Success::class.java)

        val result = exist[1].data
        assertThat(result).isFalse()
    }


    @Test
    fun createStudentWithEmptyUsername_returnFalse() = runBlockingTest {
        val student = Student(
            Account("","fullName","01012341234","password"),
            "01012341234",
            "school",
            1
        )
        val emitted = useCases.createStudent(student).toList()

        assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createStudentWithEmptyFullName_returnFalse() = runBlockingTest {
        val student = Student(
            Account("userName","","01012341234","password"),
            "01012341234",
            "school",
            1
        )
        val emitted = useCases.createStudent(student).toList()

        assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createTeacherWithEmptyUsername_returnFalse() = runBlockingTest {
        val teacher = Teacher(
            Account("","fullName","01012341234","password")
        )
        val emitted = useCases.createTeacher(teacher).toList()

        assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createTeacherWithEmptyFullName_returnFalse() = runBlockingTest {
        val teacher = Teacher(
            Account("username","","01012341234","password")
        )
        val emitted = useCases.createTeacher(teacher).toList()

        assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emitted[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun createStudent_success() = runBlockingTest {
        val student = Student(
            Account("userName","fullName","01012341234","password"),
            "01012341234",
            "school",
            1
        )
        val emitted = useCases.createStudent(student).toList()

        assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emitted[1]).isInstanceOf(Resource.Success::class.java)

        val result = emitted[1].data
        assertThat(result).isInstanceOf(Student::class.java)
    }

    @Test
    fun createTeacher_success() = runBlockingTest {
        val teacher = Teacher(
            Account("username","fullName","01012341234","password")
        )
        val emitted = useCases.createTeacher(teacher).toList()

        assertThat(emitted[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(emitted[1]).isInstanceOf(Resource.Success::class.java)

        val result = emitted[1].data
        assertThat(result).isInstanceOf(Teacher::class.java)
    }
}