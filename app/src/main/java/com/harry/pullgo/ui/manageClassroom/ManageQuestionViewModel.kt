package com.harry.pullgo.ui.manageClassroom

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Question
import com.harry.pullgo.data.repository.ManageQuestionRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class ManageQuestionViewModel @ViewModelInject constructor(
    private val manageQuestionRepository: ManageQuestionRepository
): ViewModel() {
    private val _questionsSuchExamRepository = MutableLiveData<Resource<List<Question>>>()
    val questionsSuchExamRepository: LiveData<Resource<List<Question>>> = _questionsSuchExamRepository

    private val _createQuestionRepository = MutableLiveData<Resource<Question>>()
    val createQuestionRepository: LiveData<Resource<Question>> = _createQuestionRepository

    private val _editQuestionRepository = MutableLiveData<Resource<Question>>()
    val editQuestionRepository: LiveData<Resource<Question>> = _editQuestionRepository

    private val _deleteQuestionRepository = MutableLiveData<Resource<String>>()
    val deleteQuestionMessage: LiveData<Resource<String>> = _deleteQuestionRepository

    fun getQuestionsSuchExam(examId: Long){
        _questionsSuchExamRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageQuestionRepository.getQuestionsSuchExam(examId).let{response ->
                if(response.isSuccessful){
                    _questionsSuchExamRepository.postValue(Resource.success(response.body()))
                }else{
                    _questionsSuchExamRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun createQuestion(question: Question){
        _createQuestionRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageQuestionRepository.createQuestion(question).let{response ->
                if(response.isSuccessful){
                    _createQuestionRepository.postValue(Resource.success(response.body()))
                }else{
                    _createQuestionRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun editQuestion(questionId: Long, question: Question){
        _editQuestionRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageQuestionRepository.editQuestion(questionId, question).let{response ->
                if(response.isSuccessful){
                    _editQuestionRepository.postValue(Resource.success(response.body()))
                }else{
                    _editQuestionRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun deleteQuestion(questionId: Long){
        _deleteQuestionRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageQuestionRepository.deleteQuestion(questionId).let{response ->
                if(response.isSuccessful){
                    _deleteQuestionRepository.postValue(Resource.success("문제가 삭제되었습니다"))
                }else{
                    _deleteQuestionRepository.postValue(Resource.error(response.code().toString(),"문제를 삭제하지 못했습니다"))
                }
            }
        }
    }
}