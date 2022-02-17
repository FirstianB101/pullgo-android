package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetOneStudentUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(studentId: Long): Flow<Resource<Student>> = flow{
        try{
            emit(Resource.Loading<Student>())

            val student = repository.getOneStudent(studentId)

            emit(Resource.Success<Student>(student))
        }catch (e: HttpException){
            emit(Resource.Error<Student>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Student>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}