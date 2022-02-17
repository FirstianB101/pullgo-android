package com.ich.pullgo.domain.use_case.manage_request

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.ManageRequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOwnerOfAcademyUseCase @Inject constructor(
    private val repository: ManageRequestRepository
){
    operator fun invoke(ownerId: Long): Flow<Resource<Teacher>> = flow{
        try{
            emit(Resource.Loading<Teacher>())

            val owner = repository.getOwnerOfAcademy(ownerId)
            emit(Resource.Success<Teacher>(owner))

        }catch (e: HttpException){
            emit(Resource.Error<Teacher>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Teacher>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}