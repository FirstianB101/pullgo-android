package com.harry.pullgo.ui.studentFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Exam
import com.harry.pullgo.data.repository.ExamsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentExamListViewModel(private val examsRepository: ExamsRepository): ViewModel() {
    private val _studentExamList = MutableLiveData<List<Exam>>()
    val studentExamList = _studentExamList

    fun requestExamsByName(studentId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            examsRepository.getExamsByName(studentId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentExamList.postValue(it)
                    }
                }
            }
        }
    }

    fun requestExamsByBeginDate(studentId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            examsRepository.getExamsByBeginDate(studentId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentExamList.postValue(it)
                    }
                }
            }
        }
    }

    fun requestExamsByEndDate(studentId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            examsRepository.getExamsByEndDate(studentId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentExamList.postValue(it)
                    }
                }
            }
        }
    }

    fun requestExamsByEndDateDesc(studentId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            examsRepository.getExamsByEndDateDesc(studentId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentExamList.postValue(it)
                    }
                }
            }
        }
    }
}

class StudentExamListViewModelFactory(private val examsRepository: ExamsRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ExamsRepository::class.java).newInstance(examsRepository)
    }
}