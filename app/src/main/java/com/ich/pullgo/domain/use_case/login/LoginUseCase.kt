package com.ich.pullgo.domain.use_case.login

import com.ich.pullgo.common.Constants
import com.ich.pullgo.common.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
){
    operator fun invoke(account: Account): Flow<Resource<UserWithAcademyExist>> = flow{
        val user: User?
        var userWithAcademy: UserWithAcademyExist? = null

        try{
            emit(Resource.Loading<UserWithAcademyExist>())
            user = repository.getLoginUser(account)
            userWithAcademy = UserWithAcademyExist(user,false)

            lateinit var academiesApplied: List<Academy>
            if(user.student != null){
                academiesApplied = repository.getAcademiesByStudentId(user.student?.id!!)
            }else if(user.teacher != null){
                academiesApplied = repository.getAcademiesByTeacherId(user.teacher?.id!!)
            }

            userWithAcademy.academyExist = academiesApplied.isNotEmpty()

            emit(Resource.Success<UserWithAcademyExist>(userWithAcademy))

        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT,userWithAcademy))
        }catch (e: IOException){
            emit(Resource.Error(Constants.CANNOT_CONNECT_SERVER_COMMENT,userWithAcademy))
        }
    }
}