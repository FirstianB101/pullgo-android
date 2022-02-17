package com.ich.pullgo.domain.use_case.manage_classroom.edit_classroom

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EditClassroomUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
){
    operator fun invoke(classroomId: Long,classroom: Classroom): Flow<Resource<Classroom>> = flow{
        try{
            emit(Resource.Loading<Classroom>())

            val editedClassroom = repository.editClassroom(classroomId, classroom)

            emit(Resource.Success<Classroom>(editedClassroom))
        }catch (e: HttpException){
            emit(Resource.Error<Classroom>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Classroom>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}