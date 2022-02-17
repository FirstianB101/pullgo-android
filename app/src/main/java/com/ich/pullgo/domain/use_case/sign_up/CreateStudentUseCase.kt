package com.ich.pullgo.domain.use_case.sign_up

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateStudentUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
){
    operator fun invoke(student: Student): Flow<Resource<Student>> = flow{
        var newStudent: Student? = null

        try{
            emit(Resource.Loading<Student>())

            newStudent = signUpRepository.createStudent(student)

            emit(Resource.Success<Student>(newStudent))

        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT,newStudent))
        }catch (e: IOException){
            emit(Resource.Error(Constants.CANNOT_CONNECT_SERVER_COMMENT,newStudent))
        }
    }
}