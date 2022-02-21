package com.ich.pullgo.domain.use_case.login

import com.google.common.truth.Truth.assertThat
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.repository.FakeLoginRepository
import com.ich.pullgo.domain.model.Account
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    lateinit var repository: FakeLoginRepository
    lateinit var useCase: LoginUseCase

    @Before
    fun setUp(){
        repository = FakeLoginRepository()
        useCase = LoginUseCase(repository)
    }

    @Test
    fun tryLoginWithNotAppliedUser_throwError() = runBlockingTest {
        val account = Account("not_exist_user","not_exist","","1234")
        val result = useCase(account).toList()

        assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        // account is not applied, so http error will occur
        assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun tryLoginWithStudentWithNoAppliedAcademy_returnFalse() = runBlockingTest {
        val account = Account("student","student","010","1234")
        val result = useCase(account).toList()

        assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        // login with student's account
        assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val existWithAcademy = result[1].data
        // check login user
        assertThat(existWithAcademy?.user?.student).isNotNull()
        // check applied academy is Not Exist
        assertThat(existWithAcademy?.academyExist).isFalse()
    }

    @Test
    fun tryLoginWithTeacherWithNoAppliedAcademy_returnFalse() = runBlockingTest {
        val account = Account("teacher","teacher","010","1234")
        val result = useCase(account).toList()

        assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        // login with student's account
        assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val existWithAcademy = result[1].data
        // check login user
        assertThat(existWithAcademy?.user?.teacher).isNotNull()
        // check applied academy is Not Exist
        assertThat(existWithAcademy?.academyExist).isFalse()
    }

    @Test
    fun tryLoginWithStudentWithAppliedAcademy_returnTrue() = runBlockingTest {
        val account = Account("studentAcademy","studentAcademy","010","1234")
        val result = useCase(account).toList()

        assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        // login with student's account
        assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val existWithAcademy = result[1].data
        // check login user
        assertThat(existWithAcademy?.user?.student).isNotNull()
        // check applied academy is Exist
        assertThat(existWithAcademy?.academyExist).isTrue()
    }

    @Test
    fun tryLoginWithTeacherWithAppliedAcademy_returnFalse() = runBlockingTest {
        val account = Account("teacherAcademy","teacherAcademy","010","1234")
        val result = useCase(account).toList()

        assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        // login with student's account
        assertThat(result[1]).isInstanceOf(Resource.Success::class.java)

        val existWithAcademy = result[1].data
        // check login user
        assertThat(existWithAcademy?.user?.teacher).isNotNull()
        // check applied academy is Not Exist
        assertThat(existWithAcademy?.academyExist).isTrue()
    }
}