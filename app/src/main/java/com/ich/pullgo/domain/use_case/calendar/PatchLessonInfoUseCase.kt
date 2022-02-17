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

class PatchLessonInfoUseCase @Inject constructor(
    private val repository: LessonsRepository
){
    operator fun invoke(lessonId: Long, lesson: Lesson): Flow<Resource<Lesson>> = flow{
        try{
            emit(Resource.Loading<Lesson>())

            val patchedLesson = repository.requestPatchLessonInfo(lessonId, lesson)
            emit(Resource.Success<Lesson>(patchedLesson))

        }catch (e: HttpException){
            emit(Resource.Error<Lesson>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Lesson>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}