package com.ich.pullgo.ui.studentFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/studentFragment/StudentExamListViewModel.kt
import com.ich.pullgo.data.models.AttenderState
import com.ich.pullgo.data.models.CreateAttender
import com.ich.pullgo.data.models.Exam
import com.ich.pullgo.data.repository.ExamsRepository
import com.ich.pullgo.data.utils.Resource
=======
import com.ich.pullgo.data.remote.dto.CreateAttender
import com.ich.pullgo.data.repository.ExamsRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/studentFragment/StudentExamListViewModel.kt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentExamListViewModel @Inject constructor(
    private val examsRepository: ExamsRepository
    ): ViewModel() {
    private val _studentExamList = MutableLiveData<Resource<List<Exam>>>()
    val studentExamList: LiveData<Resource<List<Exam>>> = _studentExamList

    private val _startExamAttenderState = MutableLiveData<Resource<AttenderState>>()
    val startExamAttenderState: LiveData<Resource<AttenderState>> = _startExamAttenderState

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

    fun startExamByMakingState(attender: CreateAttender){
        _startExamAttenderState.postValue(Resource.loading(null))

        viewModelScope.launch {
            examsRepository.startTakingExam(attender).let { response ->
                if(response.isSuccessful){
                    _startExamAttenderState.postValue(Resource.success(response.body()))
                }else{
                    _startExamAttenderState.postValue(Resource.error(response.code().toString(),null))
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