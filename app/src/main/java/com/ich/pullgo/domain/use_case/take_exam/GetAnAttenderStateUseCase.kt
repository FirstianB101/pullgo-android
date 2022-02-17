package com.ich.pullgo.domain.use_case.take_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.repository.TakeExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAnAttenderStateUseCase @Inject constructor(
    private val repository: TakeExamRepository
){
    operator fun invoke(attenderStateId: Long): Flow<Resource<AttenderState>> = flow{
        try{
            emit(Resource.Loading<AttenderState>())
            val attenderState = repository.getOneAttenderState(attenderStateId)

            emit(Resource.Success<AttenderState>(attenderState))
        }catch (e: HttpException){
            emit(Resource.Error<AttenderState>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<AttenderState>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}