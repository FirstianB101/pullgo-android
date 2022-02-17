package com.ich.pullgo.domain.use_case.manage_academy

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.ManageAcademyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStudentsInAcademyUseCase @Inject constructor(
    private val repository: ManageAcademyRepository
){
    operator fun invoke(academyId: Long): Flow<Resource<List<Student>>> = flow{
        try{
            emit(Resource.Loading<List<Student>>())

            val students = repository.getStudentsSuchAcademy(academyId)

            emit(Resource.Success<List<Student>>(students))
        }catch (e: HttpException){
            emit(Resource.Error<List<Student>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Student>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}