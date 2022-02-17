package com.ich.pullgo.domain.use_case.calendar

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.repository.LessonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetClassroomSuchLessonUseCase @Inject constructor(
    private val repository: LessonsRepository
){
    operator fun invoke(classroomId: Long): Flow<Resource<Classroom>> = flow{
        try{
            emit(Resource.Loading<Classroom>())

            val classroom = repository.getClassroomSuchLesson(classroomId)
            emit(Resource.Success<Classroom>(classroom))

        }catch (e: HttpException){
            emit(Resource.Error<Classroom>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Classroom>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}