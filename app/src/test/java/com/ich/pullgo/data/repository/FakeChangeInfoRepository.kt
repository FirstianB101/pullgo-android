package com.ich.pullgo.data.repository

import com.ich.pullgo.domain.model.*
import com.ich.pullgo.domain.repository.ChangeInfoRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeChangeInfoRepository: ChangeInfoRepository {

    val users = mutableListOf<User>()

    init {

    }

    override suspend fun changeStudentInfo(studentId: Long, student: Student): Student {
        for(user in users){
            if(user.student?.id == studentId){
                user.student!!.copy(student)
                return student
            }
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }

    override suspend fun changeTeacherInfo(teacherId: Long, teacher: Teacher): Teacher {
        for(user in users){
            if(user.teacher?.id == teacherId){
                user.teacher!!.copy(teacher)
                return teacher
            }
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }

    override suspend fun authUser(account: Account): User {
        for(user in users){
            when{
                user.student != null -> {
                    if(user.student!!.account?.username == account.username &&
                        user.student!!.account?.password == account.password){
                        return user
                    }
                }
                user.teacher != null -> {
                    if(user.teacher!!.account?.username == account.username &&
                        user.teacher!!.account?.password == account.password){
                        return user
                    }
                }
            }
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }
}