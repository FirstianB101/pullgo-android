package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.manage_question

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.ImageUploadResponse
import com.ich.pullgo.domain.repository.ManageQuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UploadQuestionImageUseCase @Inject constructor(
    private val repository: ManageQuestionRepository
){
    operator fun invoke(image: RequestBody): Flow<Resource<ImageUploadResponse>> = flow{
        try{
            emit(Resource.Loading<ImageUploadResponse>())

            val response = repository.requestUploadImage(image)

            emit(Resource.Success<ImageUploadResponse>(response))
        }catch (e: HttpException){
            emit(Resource.Error<ImageUploadResponse>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<ImageUploadResponse>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}