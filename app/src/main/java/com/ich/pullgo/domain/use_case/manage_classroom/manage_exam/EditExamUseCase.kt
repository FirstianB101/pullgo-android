package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam

import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EditExamUseCase @Inject constructor(
    private val repository: ManageClassroomRepository
) {
    operator fun invoke(examId: Long, exam: Exam): Flow<Resource<Exam>> = flow{
        try{
            emit(Resource.Loading<Exam>())

            val edited = repository.editExam(examId, exam)

            emit(Resource.Success<Exam>(edited))
        }catch (e: HttpException){
            emit(Resource.Error<Exam>(e.localizedMessage ?: Constants.HTTP_EXCEPTION_COMMENT))
        }catch (e: IOException){
            emit(Resource.Error<Exam>(Constants.CANNOT_CONNECT_SERVER_COMMENT))
        }
    }
}