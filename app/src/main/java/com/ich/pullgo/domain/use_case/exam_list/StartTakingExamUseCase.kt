package com.ich.pullgo.domain.use_case.exam_list

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.repository.ExamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StartTakingExamUseCase @Inject constructor(
    private val repository: ExamsRepository
){
    operator fun invoke(attender: CreateAttender): Flow<Resource<AttenderState>> = flow{
        try{
            emit(Resource.Loading<AttenderState>())

            val state = repository.startTakingExam(attender)
            emit(Resource.Success<AttenderState>(state))

        }catch (e: HttpException){
            emit(Resource.Error<AttenderState>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<AttenderState>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}