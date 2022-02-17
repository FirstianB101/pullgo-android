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

class GetSearchedAcademiesUseCase @Inject constructor(
    private val repository: ApplyAcademyRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<Academy>>> = flow{
        try{
            emit(Resource.Loading<List<Academy>>())

            val academies = repository.getAcademies(query)
            emit(Resource.Success<List<Academy>>(academies))

        }catch (e: HttpException){
            emit(Resource.Error<List<Academy>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Academy>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}