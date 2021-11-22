package com.harry.pullgo.ui.examStatus

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.AttenderState
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.repository.ExamStatusRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class ManageExamStatusViewModel @ViewModelInject constructor(
    private val repository: ExamStatusRepository
): ViewModel() {
    private val _attenderStatesInExam = MutableLiveData<Resource<List<AttenderState>>>()
    val attenderStatesInExam: LiveData<Resource<List<AttenderState>>> = _attenderStatesInExam

    private val _oneStudent = MutableLiveData<Resource<Student>>()
    val oneStudent: LiveData<Resource<Student>> = _oneStudent

    fun requestGetAttenderStatesInExam(examId: Long){
        _attenderStatesInExam.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getAttenderStatesInExam(examId).let { response ->
                if(response.isSuccessful){
                    _attenderStatesInExam.postValue(Resource.success(response.body()))
                }else{
                    _attenderStatesInExam.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getOneStudent(studentId: Long){
        viewModelScope.launch {
            repository.getOneStudent(studentId).let{response ->
                if(response.isSuccessful){
                    _oneStudent.postValue(Resource.success(response.body()))
                }else{
                    _oneStudent.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}