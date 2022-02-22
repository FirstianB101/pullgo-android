package com.ich.pullgo.domain.use_case.change_info

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.domain.repository.ChangeInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PwCheckUseCase @Inject constructor(
    private val repository: ChangeInfoRepository
) {
    operator fun invoke(curUser: User, password: String): Flow<Resource<User>> = flow{
        try{
            emit(Resource.Loading<User>())

            val account = if(curUser.student != null)
                Account(
                    curUser.student?.account?.username,
                    curUser.student?.account?.fullName,
                    curUser.student?.account?.phone,
                    password
                )
            else
                Account(
                    curUser.teacher?.account?.username,
                    curUser.teacher?.account?.fullName,
                    curUser.teacher?.account?.phone,
                    password
                )

            val user = repository.authUser(account)

            emit(Resource.Success<User>(user))
        }catch (e: HttpException){
            emit(Resource.Error<User>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT, null))
        }catch (e: IOException){
            emit(Resource.Error<User>(Constants.CANNOT_CONNECT_SERVER_COMMENT, null))
        }
    }
}