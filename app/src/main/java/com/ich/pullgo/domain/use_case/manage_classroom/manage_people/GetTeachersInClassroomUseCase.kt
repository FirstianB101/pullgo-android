package com.ich.pullgo.domain.use_case.manage_classroom.manage_people

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetTeachersInClassroomUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(classroomId: Long): Flow<Resource<List<Teacher>>> = flow{
        try{
            emit(Resource.Loading<List<Teacher>>())

            val teachers = repository.getTeachersAppliedClassroom(classroomId)

            emit(Resource.Success<List<Teacher>>(teachers))
        }catch (e: HttpException){
            emit(Resource.Error<List<Teacher>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Teacher>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}