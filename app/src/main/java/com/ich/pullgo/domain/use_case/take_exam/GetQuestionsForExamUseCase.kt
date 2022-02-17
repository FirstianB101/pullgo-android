package com.ich.pullgo.domain.use_case.take_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.domain.repository.TakeExamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetQuestionsForExamUseCase @Inject constructor(
    private val repository: TakeExamRepository
){
    operator fun invoke(examId: Long): Flow<Resource<List<Question>>> = flow{
        try{
            emit(Resource.Loading<List<Question>>())
            val questions = repository.getQuestionsSuchExam(examId)

            emit(Resource.Success<List<Question>>(questions))

        }catch (e: HttpException){
            emit(Resource.Error<List<Question>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Question>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}