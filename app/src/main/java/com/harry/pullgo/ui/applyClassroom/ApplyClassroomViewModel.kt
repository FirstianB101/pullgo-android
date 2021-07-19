package com.harry.pullgo.ui.applyClassroom

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.repository.ApplyClassroomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyClassroomViewModel(private val applyClassroomRepository: ApplyClassroomRepository): ViewModel() {
    private val _appliedAcademiesRepository = MutableLiveData<List<Academy>>()
    val appliedAcademiesRepository = _appliedAcademiesRepository

    private val _applyClassroomRepositories = MutableLiveData<List<Classroom>>()
    val applyClassroomsRepositories = _applyClassroomRepositories

    fun requestStudentAppliedAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            applyClassroomRepository.getAcademiesStudentApplied(id).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                        _appliedAcademiesRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherAppliedAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            applyClassroomRepository.getAcademiesTeacherApplied(id).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                        _appliedAcademiesRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetClassrooms(academyId: Long, name: String){
        CoroutineScope(Dispatchers.IO).launch {
            applyClassroomRepository.getClassroomsByNameAndAcademyID(academyId,name).let{ response ->
                if(response.isSuccessful){
                    if(response.isSuccessful){
                        response.body().let{
                            _applyClassroomRepositories.postValue(it)
                        }
                    }
                }
            }
        }
    }
}

class ApplyClassroomViewModelFactory(private val applyClassroomRepository: ApplyClassroomRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ApplyClassroomRepository::class.java).newInstance(applyClassroomRepository)
    }
}