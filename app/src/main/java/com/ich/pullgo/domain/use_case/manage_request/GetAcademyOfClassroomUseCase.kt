package com.ich.pullgo.domain.use_case.manage_request

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.repository.ManageRequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAcademyOfClassroomUseCase @Inject constructor(
    private val repository: ManageRequestRepository
){
    operator fun invoke(academyId: Long): Flow<Resource<Academy>> = flow{
        try{
            emit(Resource.Loading<Academy>())

            val academy = repository.getAcademyOfClassroom(academyId)
            emit(Resource.Success<Academy>(academy))

        }catch (e: HttpException){
            emit(Resource.Error<Academy>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Academy>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}