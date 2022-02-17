package com.ich.pullgo.domain.use_case.manage_request

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.repository.ManageRequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetClassroomsStudentApplyingUseCase @Inject constructor(
    private val repository: ManageRequestRepository
){
    operator fun invoke(studentId: Long): Flow<Resource<List<Classroom>>> = flow{
        try{
            emit(Resource.Loading<List<Classroom>>())

            val classrooms = repository.getClassroomsStudentApplying(studentId)

            emit(Resource.Success<List<Classroom>>(classrooms))

        }catch (e: HttpException){
            emit(Resource.Error<List<Classroom>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Classroom>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}