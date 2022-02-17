package com.ich.pullgo.domain.use_case.change_info

import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.repository.ChangeInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PwCheckUseCase @Inject constructor(
    private val repository: ChangeInfoRepository
) {
    operator fun invoke(password: String): Flow<Resource<Boolean>> = flow{

        try{
            emit(Resource.Loading<Boolean>())

            val curUser = PullgoApplication.instance!!.getLoginUser()!!

            val account: Account = if(curUser.student != null)
                Account(curUser.student?.account?.username!!, null,null, password)
            else
                Account(curUser.teacher?.account?.username!!, null, null, password)

            val user = repository.authUser(account)
            curUser.token = user.token

            emit(Resource.Success<Boolean>(true))
        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT, false))
        }catch (e: IOException){
            emit(Resource.Error(Constants.CANNOT_CONNECT_SERVER_COMMENT, false))
        }
    }
}