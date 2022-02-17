package com.ich.pullgo.domain.use_case.accept_apply_academy

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.AcceptApplyAcademyRepository
import com.ich.pullgo.domain.repository.ApplyClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStudentsApplyingAcademyUseCase @Inject constructor(
    private val repository: AcceptApplyAcademyRepository
){
    operator fun invoke(academyId: Long): Flow<Resource<List<Student>>> = flow{
        try{
            emit(Resource.Loading<List<Student>>())

            val students = repository.getStudentsApplyingAcademy(academyId)
            emit(Resource.Success<List<Student>>(students))

        }catch (e: HttpException){
            emit(Resource.Error<List<Student>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Student>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}