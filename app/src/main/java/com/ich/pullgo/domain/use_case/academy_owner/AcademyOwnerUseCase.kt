package com.ich.pullgo.domain.use_case.academy_owner

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.repository.AcademyOwnerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AcademyOwnerUseCase @Inject constructor(
    private val repository: AcademyOwnerRepository
){
    operator fun invoke(teacherId: Long): Flow<Resource<Boolean>> = flow{
        try{
            emit(Resource.Loading<Boolean>())
            val ownedAcademies = repository.getAcademiesTeacherOwned(teacherId)

            if(ownedAcademies.isNotEmpty()){
                emit(Resource.Success<Boolean>(true))
            }else{
                emit(Resource.Success<Boolean>(false))
            }

        }catch (e: HttpException){
            emit(Resource.Error<Boolean>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Boolean>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}