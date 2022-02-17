package com.ich.pullgo.domain.use_case.take_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Answer
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.repository.TakeExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SaveAttenderAnswerUseCase @Inject constructor(
    private val repository: TakeExamRepository
){
    operator fun invoke(attenderStateId: Long, questionId: Long, answer: Answer): Flow<Resource<AttenderAnswer>> = flow{
        try{
            emit(Resource.Loading<AttenderAnswer>())
            val attenderAnswer = repository.saveAttenderAnswer(attenderStateId, questionId, answer)

            emit(Resource.Success<AttenderAnswer>(attenderAnswer))
        }catch (e: HttpException){
            emit(Resource.Error<AttenderAnswer>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<AttenderAnswer>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}