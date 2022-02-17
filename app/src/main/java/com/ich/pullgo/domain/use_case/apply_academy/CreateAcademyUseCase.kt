package com.ich.pullgo.domain.use_case.apply_academy

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.repository.ApplyAcademyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateAcademyUseCase @Inject constructor(
    private val repository: ApplyAcademyRepository
){
    operator fun invoke(newAcademy: Academy): Flow<Resource<Academy>> = flow{
        try{
            emit(Resource.Loading<Academy>())

            val academy = repository.createAcademy(newAcademy)
            emit(Resource.Success<Academy>(academy))

        }catch (e: HttpException){
            emit(Resource.Error<Academy>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Academy>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}