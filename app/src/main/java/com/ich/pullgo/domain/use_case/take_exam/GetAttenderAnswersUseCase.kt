package com.ich.pullgo.domain.use_case.take_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.repository.TakeExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAttenderAnswersUseCase @Inject constructor(
    private val repository: TakeExamRepository
){
    operator fun invoke(attenderStateId: Long): Flow<Resource<List<AttenderAnswer>>> = flow{
        try{
            emit(Resource.Loading<List<AttenderAnswer>>())
            val attenderAnswers = repository.getAttenderAnswers(attenderStateId)

            emit(Resource.Success<List<AttenderAnswer>>(attenderAnswers))
        }catch (e: HttpException){
            emit(Resource.Error<List<AttenderAnswer>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<AttenderAnswer>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}