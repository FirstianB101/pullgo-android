package com.ich.pullgo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ich.pullgo.data.repository.AppliedAcademyGroupRepository
import com.ich.pullgo.domain.model.Academy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppliedAcademyGroupViewModel(private val appliedAcademiesRepository: AppliedAcademyGroupRepository):ViewModel() {
    private val _appliedAcademiesRepositories = MutableLiveData<List<Academy>>()
    val appliedAcademiesRepositories = _appliedAcademiesRepositories

    fun requestStudentAppliedAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch{
            appliedAcademiesRepository.getStudentAppliedAcademies(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _appliedAcademiesRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherAppliedAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch{
            appliedAcademiesRepository.getTeacherAppliedAcademies(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _appliedAcademiesRepositories.postValue(it)
                    }
                }
            }
        }
    }
}