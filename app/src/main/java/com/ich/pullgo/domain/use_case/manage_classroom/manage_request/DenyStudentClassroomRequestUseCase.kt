package com.ich.pullgo.domain.use_case.manage_classroom.manage_request

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DenyStudentClassroomRequestUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(classroomId: Long, studentId: Long): Flow<Resource<Boolean>> = flow{
        try{
            emit(Resource.Loading<Boolean>())

            repository.denyStudent(studentId, classroomId)

            emit(Resource.Success<Boolean>(true))
        }catch (e: HttpException){
            emit(Resource.Error<Boolean>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Boolean>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}