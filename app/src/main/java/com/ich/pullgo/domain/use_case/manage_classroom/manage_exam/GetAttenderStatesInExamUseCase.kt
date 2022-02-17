package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAttenderStatesInExamUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(examId: Long): Flow<Resource<List<AttenderState>>> = flow{
        try{
            emit(Resource.Loading<List<AttenderState>>())

            val attenderStates = repository.getAttenderStatesInExam(examId)

            emit(Resource.Success<List<AttenderState>>(attenderStates))
        }catch (e: HttpException){
            emit(Resource.Error<List<AttenderState>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<AttenderState>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}