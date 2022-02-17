package com.ich.pullgo.domain.use_case.sign_up

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CreateTeacherUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
){
    operator fun invoke(teacher: Teacher): Flow<Resource<Teacher>> = flow{
        var newTeacher: Teacher? = null

        try{
            emit(Resource.Loading<Teacher>())

            newTeacher = signUpRepository.createTeacher(teacher)

            emit(Resource.Success<Teacher>(newTeacher))

        }catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT,newTeacher))
        }catch (e: IOException){
            emit(Resource.Error(Constants.CANNOT_CONNECT_SERVER_COMMENT,newTeacher))
        }
    }
}