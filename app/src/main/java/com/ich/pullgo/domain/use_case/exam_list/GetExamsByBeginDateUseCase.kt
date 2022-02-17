package com.ich.pullgo.domain.use_case.exam_list

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.repository.ExamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetExamsByBeginDateUseCase @Inject constructor(
    private val repository: ExamsRepository
){
    operator fun invoke(studentId: Long): Flow<Resource<List<Exam>>> = flow{
        try{
            emit(Resource.Loading<List<Exam>>())

            val exams = repository.getExamsByBeginDate(studentId)
            emit(Resource.Success<List<Exam>>(exams))

        }catch (e: HttpException){
            emit(Resource.Error<List<Exam>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Exam>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}