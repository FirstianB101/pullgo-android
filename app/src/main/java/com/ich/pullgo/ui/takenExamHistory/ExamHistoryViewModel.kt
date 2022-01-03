package com.ich.pullgo.ui.takenExamHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/takenExamHistory/ExamHistoryViewModel.kt
import com.ich.pullgo.data.models.AttenderAnswer
import com.ich.pullgo.data.models.Question
import com.ich.pullgo.data.repository.ExamHistoryRepository
import com.ich.pullgo.data.utils.Resource
=======
import com.ich.pullgo.data.repository.ExamHistoryRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.AttenderAnswer
import com.ich.pullgo.domain.model.Question
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/takenExamHistory/ExamHistoryViewModel.kt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamHistoryViewModel @Inject constructor(
    private val repository: ExamHistoryRepository
): ViewModel() {
    private val _questionsSuchExamRepository = MutableLiveData<Resource<List<Question>>>()
    val questionsSuchExamRepository: LiveData<Resource<List<Question>>> = _questionsSuchExamRepository

    private val _attenderAnswersRepository = MutableLiveData<Resource<List<AttenderAnswer>>>()
    val attenderAnswersRepository: LiveData<Resource<List<AttenderAnswer>>> = _attenderAnswersRepository

    fun getQuestionsSuchExam(examId: Long){
        _questionsSuchExamRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getQuestionsSuchExam(examId).let{ response ->
                if(response.isSuccessful){
                    _questionsSuchExamRepository.postValue(Resource.success(response.body()))
                }else{
                    _questionsSuchExamRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getAttenderAnswers(attenderStateId: Long){
        _attenderAnswersRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getAttenderAnswers(attenderStateId).let { response ->
                if(response.isSuccessful){
                    _attenderAnswersRepository.postValue(Resource.success(response.body()))
                }else{
                    _attenderAnswersRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}