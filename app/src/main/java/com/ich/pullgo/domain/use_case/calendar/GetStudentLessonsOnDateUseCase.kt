package com.ich.pullgo.domain.use_case.calendar

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.domain.repository.LessonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStudentLessonsOnDateUseCase @Inject constructor(
    private val repository: LessonsRepository
){
    operator fun invoke(studentId: Long, date: String): Flow<Resource<List<Lesson>>> = flow{
        try{
            emit(Resource.Loading<List<Lesson>>())

            val lessons = repository.getStudentLessonsOnDate(studentId, date)
            emit(Resource.Success<List<Lesson>>(lessons))

        }catch (e: HttpException){
            emit(Resource.Error<List<Lesson>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Lesson>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}