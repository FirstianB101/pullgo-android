package com.harry.pullgo.ui.takenExamHistory

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Answer
import com.harry.pullgo.data.models.AttenderAnswer
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.repository.TakeExamRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class ExamHistoryViewModel @ViewModelInject constructor(
    private val repository: TakeExamRepository
): ViewModel() {
    private val _oneQuestionRepository = MutableLiveData<Resource<Question>>()
    val oneQuestionRepository: LiveData<Resource<Question>> = _oneQuestionRepository

    private val _questionsSuchExamRepository = MutableLiveData<Resource<List<Question>>>()
    val questionsSuchExamRepository: LiveData<Resource<List<Question>>> = _questionsSuchExamRepository

    private val _attenderAnswerRepository = MutableLiveData<Resource<AttenderAnswer>>()
    val attenderAnswerRepository: LiveData<Resource<AttenderAnswer>> = _attenderAnswerRepository

    private val _takeExamMessage = MutableLiveData<Resource<String>>()
    val takeExamMessage = _takeExamMessage

    fun getOneQuestion(questionId: Long){
        _oneQuestionRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getOneQuestion(questionId).let{ response ->
                if(response.isSuccessful){
                    _oneQuestionRepository.postValue(Resource.success(response.body()))
                }else{
                    _oneQuestionRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

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

    fun saveAttenderAnswer(attenderStateId: Long, questionId: Long, answer: Answer){
        _attenderAnswerRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.saveAttenderAnswer(attenderStateId, questionId, answer).let{response ->
                if(response.isSuccessful){
                    _attenderAnswerRepository.postValue(Resource.success(response.body()))
                }else{
                    _attenderAnswerRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun submitAttenderState(attenderStateId: Long){
        viewModelScope.launch {
            repository.submitAttenderState(attenderStateId).let{response ->
                if(response.isSuccessful){
                    _takeExamMessage.postValue(Resource.success("응시가 완료되었습니다"))
                }else{
                    _takeExamMessage.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}