package com.ich.pullgo.domain.use_case.manage_classroom.manage_request

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStudentClassroomRequestsUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(classroomId: Long): Flow<Resource<List<Student>>> = flow{
        try{
            emit(Resource.Loading<List<Student>>())

            val students = repository.getStudentsRequestApplyClassroom(classroomId)

            emit(Resource.Success<List<Student>>(students))
        }catch (e: HttpException){
            emit(Resource.Error<List<Student>>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<List<Student>>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}