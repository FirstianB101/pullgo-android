package com.harry.pullgo.ui.takenExamHistory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Answer
import com.harry.pullgo.data.models.AttenderAnswer
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.repository.ExamHistoryRepository
import com.harry.pullgo.data.repository.TakeExamRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class ExamHistoryViewModel @ViewModelInject constructor(
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