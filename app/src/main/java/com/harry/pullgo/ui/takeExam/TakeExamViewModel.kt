package com.harry.pullgo.ui.takeExam

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.repository.TakeExamRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class TakeExamViewModel @ViewModelInject constructor(
    private val repository: TakeExamRepository
): ViewModel() {
    private val _oneQuestionRepository = MutableLiveData<Resource<Question>>()
    val oneQuestionRepository: LiveData<Resource<Question>> = _oneQuestionRepository

    private val _questionsSuchExamRepository = MutableLiveData<Resource<List<Question>>>()
    val questionsSuchExamRepository: LiveData<Resource<List<Question>>> = _questionsSuchExamRepository

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
}