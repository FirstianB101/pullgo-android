package com.ich.pullgo.domain.use_case.change_info

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.ChangeInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ChangeTeacherInfoUseCase @Inject constructor(
    private val repository: ChangeInfoRepository
){
    operator fun invoke(teacher: Teacher): Flow<Resource<Teacher>> = flow{
        try{
            emit(Resource.Loading<Teacher>())

            val modified = repository.changeTeacherInfo(teacher.id!!, teacher)

            emit(Resource.Success<Teacher>(modified))

        }catch (e: HttpException){
            emit(Resource.Error<Teacher>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Teacher>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}