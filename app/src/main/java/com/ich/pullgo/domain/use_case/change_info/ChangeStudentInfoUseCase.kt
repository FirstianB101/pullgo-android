package com.ich.pullgo.domain.use_case.change_info

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.ChangeInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ChangeStudentInfoUseCase @Inject constructor(
    private val repository: ChangeInfoRepository
){
    operator fun invoke(student: Student): Flow<Resource<Student>> = flow{
        try{
            emit(Resource.Loading<Student>())

            val modified = repository.changeStudentInfo(student.id!!,student)

            emit(Resource.Success<Student>(modified))

        }catch (e: HttpException){
            emit(Resource.Error<Student>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Student>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}