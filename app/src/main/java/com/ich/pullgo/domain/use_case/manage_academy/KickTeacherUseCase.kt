package com.ich.pullgo.domain.use_case.manage_academy

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.repository.ManageAcademyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class KickTeacherUseCase @Inject constructor(
    private val repository: ManageAcademyRepository
) {
    operator fun invoke(academyId: Long, teacherId: Long): Flow<Resource<Boolean>> = flow{
        try{
            emit(Resource.Loading<Boolean>())

            repository.kickTeacher(academyId, teacherId)

            emit(Resource.Success<Boolean>(true))
        }catch (e: HttpException){
            emit(Resource.Error<Boolean>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Boolean>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}