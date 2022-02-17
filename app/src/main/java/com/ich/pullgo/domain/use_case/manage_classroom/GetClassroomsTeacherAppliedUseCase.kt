package com.ich.pullgo.domain.use_case.manage_classroom

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetClassroomsTeacherAppliedUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(teacherId: Long): Flow<Resource<List<Classroom>>> = flow{
        try{
            emit(Resource.Loading<List<Classroom>>())

            val classrooms = repository.getClassroomsTeacherApplied(teacherId)

            emit(Resource.Success<List<Classroom>>(classrooms))
        }catch (e: HttpException){
            emit(Resource.Error<List<Classroom>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Classroom>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}