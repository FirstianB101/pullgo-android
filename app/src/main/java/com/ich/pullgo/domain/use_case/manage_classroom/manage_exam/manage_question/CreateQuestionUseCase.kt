package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.manage_question

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Question
import com.ich.pullgo.domain.repository.ManageQuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateQuestionUseCase @Inject constructor(
    private val repository: ManageQuestionRepository
){
    operator fun invoke(question: Question): Flow<Resource<Question>> = flow{
        try{
            emit(Resource.Loading<Question>())

            val newQuestion = repository.createQuestion(question)

            emit(Resource.Success<Question>(newQuestion))
        }catch (e: HttpException){
            emit(Resource.Error<Question>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Question>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}