package com.ich.pullgo.data.repository

import com.ich.pullgo.domain.model.*
import com.ich.pullgo.domain.repository.LoginRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeLoginRepository: LoginRepository {

    val EXIST_STUDENT_ID = 1L
    val EXIST_STUDENT_ID_ACADEMY_APPLIED = 2L
    val EXIST_TEACHER_ID = 3L
    val EXIST_TEACHER_ID_ACADEMY_APPLIED = 4L

    val users = mutableListOf<User>()

    init {
        val studentUser = User()
        studentUser.student = Student(Account("student","student","010","1234"),"010","school",1)
        studentUser.student!!.id = EXIST_STUDENT_ID

        val studentUserWithAcademy = User()
        studentUserWithAcademy.student = Student(Account("studentAcademy","studentAcademy","010","1234"),"010","school",1)
        studentUserWithAcademy.student!!.id = EXIST_STUDENT_ID_ACADEMY_APPLIED

        val teacherUser = User()
        teacherUser.teacher = Teacher(Account("teacher","teacher","010","1234"))
        teacherUser.teacher!!.id = EXIST_TEACHER_ID

        val teacherUserWithAcademy = User()
        teacherUserWithAcademy.teacher = Teacher(Account("teacherAcademy","teacherAcademy","010","1234"))
        teacherUserWithAcademy.teacher!!.id = EXIST_TEACHER_ID_ACADEMY_APPLIED

        users.add(studentUser)
        users.add(studentUserWithAcademy)
        users.add(teacherUser)
        users.add(teacherUserWithAcademy)
    }

    override suspend fun getLoginUser(account: Account): User {
        for(i in users.indices){
            val user = users[i]
            when{
                user.student != null -> {
                    if(user.student!!.account?.username == account.username &&
                        user.student!!.account?.password == account.password)
                        return user

                }
                user.teacher != null -> {
                    if(user.teacher!!.account?.username == account.username &&
                        user.teacher!!.account?.password == account.password)
                        return user
                }
            }
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }

    override suspend fun getAutoLoginUser(): User {
        return User()
    }

    override suspend fun getAcademiesByStudentId(id: Long): List<Academy> {
        return if(id == EXIST_STUDENT_ID_ACADEMY_APPLIED) listOf(Academy("","","",null))
        else emptyList()
    }

    override suspend fun getAcademiesByTeacherId(id: Long): List<Academy> {
        return if(id == EXIST_TEACHER_ID_ACADEMY_APPLIED) listOf(Academy("","","",null))
        else emptyList()
    }
}