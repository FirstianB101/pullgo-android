package com.ich.pullgo.domain.use_case.change_info

import com.google.common.truth.Truth
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeChangeInfoRepository
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class PwCheckUseCaseTest {

    lateinit var repository: FakeChangeInfoRepository
    lateinit var useCase: PwCheckUseCase

    val STUDENT_USER_ID = 1L
    val TEACHER_USER_ID = 2L

    @Before
    fun setUp() {
        repository = FakeChangeInfoRepository()
        useCase = PwCheckUseCase(repository)
    }

    @Test
    fun inputInvalidPassword_returnError() = runBlockingTest {
        val curUser = User().also { user ->
            user.student = Student(Account("student","student","","1234"),"","",null)
                .also { it.id = STUDENT_USER_ID }
        }

        val result = useCase(curUser,"0000").toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun studentInputValidPassword_returnSuccess() = runBlockingTest {
        val curUser = User().also { user ->
            user.student = Student(Account("student","student","","1234"),"","",null)
                .also { it.id = STUDENT_USER_ID }
        }

        val result = useCase(curUser, "1234").toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        Truth.assertThat(result[1].data?.student?.id).isEqualTo(STUDENT_USER_ID)
    }

    @Test
    fun teacherInputValidPassword_returnSuccess() = runBlockingTest {
        val curUser = User().also { user ->
            user.teacher = Teacher(Account("teacher","teacher","","1234"))
                .also { it.id = TEACHER_USER_ID }
        }

        val result = useCase(curUser, "1234").toList()

        Truth.assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        Truth.assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        Truth.assertThat(result[1].data?.teacher?.id).isEqualTo(TEACHER_USER_ID)
    }
}