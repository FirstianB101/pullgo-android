package com.ich.pullgo.domain.use_case.exam_list

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.repository.ExamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStatesByStudentIdUseCase @Inject constructor(
    private val repository: ExamsRepository
){
    operator fun invoke(studentId: Long): Flow<Resource<List<AttenderState>>> = flow{
        try{
            emit(Resource.Loading<List<AttenderState>>())

            val states = repository.getStatesByStudentId(studentId)
            emit(Resource.Success<List<AttenderState>>(states))

        }catch (e: HttpException){
            emit(Resource.Error<List<AttenderState>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<AttenderState>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}