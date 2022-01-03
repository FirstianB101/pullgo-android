package com.ich.pullgo.domain.use_case.sign_up

import com.ich.pullgo.common.Constants
import com.ich.pullgo.common.Resource
import com.ich.pullgo.data.remote.dto.ExistDto
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.repository.SignUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CheckIdExistUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {
    operator fun invoke(username: String): Flow<Resource<Boolean>> = flow{
        try{
            emit(Resource.Loading<Boolean>())

            var exist: Boolean = false
            exist = exist || signUpRepository.studentUsernameExists(username).exists
            exist = exist || signUpRepository.teacherUsernameExists(username).exists

            emit(Resource.Success<Boolean>(exist))
        }catch (e: HttpException){
            emit(Resource.Error<Boolean>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Boolean>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}