package com.harry.pullgo.ui.studentFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.harry.pullgo.data.models.AttenderState
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ExamsRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentExamListViewModel @ViewModelInject constructor(
    private val examsRepository: ExamsRepository
    ): ViewModel() {
    private val _studentExamList = MutableLiveData<Resource<List<Exam>>>()
    val studentExamList: LiveData<Resource<List<Exam>>> = _studentExamList

    private val _studentAttenderStates = MutableLiveData<Resource<List<AttenderState>>>()
    val studentAttenderStates: LiveData<Resource<List<AttenderState>>> = _studentAttenderStates

    fun requestExamsByName(studentId: Long){
        _studentExamList.postValue(Resource.loading(null))

        viewModelScope.launch {
            examsRepository.getExamsByName(studentId).let{ response ->
                if(response.isSuccessful){
                    _studentExamList.postValue(Resource.success(response.body()))
                }else{
                    _studentExamList.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestExamsByBeginDate(studentId: Long){
        _studentExamList.postValue(Resource.loading(null))

        viewModelScope.launch {
            examsRepository.getExamsByBeginDate(studentId).let{ response ->
                if(response.isSuccessful){
                    _studentExamList.postValue(Resource.success(response.body()))
                }else{
                    _studentExamList.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestExamsByEndDate(studentId: Long){
        _studentExamList.postValue(Resource.loading(null))

        viewModelScope.launch {
            examsRepository.getExamsByEndDate(studentId).let{ response ->
                if(response.isSuccessful){
                    _studentExamList.postValue(Resource.success(response.body()))
                }else{
                    _studentExamList.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestExamsByEndDateDesc(studentId: Long){
        _studentExamList.postValue(Resource.loading(null))

        viewModelScope.launch {
            examsRepository.getExamsByEndDateDesc(studentId).let{ response ->
                if(response.isSuccessful){
                    _studentExamList.postValue(Resource.success(response.body()))
                }else{
                    _studentExamList.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getStudentAttenderStates(studentId: Long){
        _studentAttenderStates.postValue(Resource.loading(null))

        viewModelScope.launch {
            examsRepository.getStatesByStudentId(studentId).let{ response ->
                if(response.isSuccessful){
                    _studentAttenderStates.postValue(Resource.success(response.body()))
                }else{
                    _studentAttenderStates.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}