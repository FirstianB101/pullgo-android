package com.harry.pullgo.ui.teacherFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.repository.ClassroomsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageClassroomViewModel(private val classroomsRepository: ClassroomsRepository): ViewModel() {
    private val _selectedClassroom = MutableLiveData<Classroom>()
    val selectedClassroom: LiveData<Classroom> = _selectedClassroom

    private val _getClassroomRepositories = MutableLiveData<List<Classroom>>()
    val getClassroomRepositories: LiveData<List<Classroom>> = _getClassroomRepositories

    private val _academiesForSpinnerRepository = MutableLiveData<List<Academy>>()
    val academiesForSpinnerRepository: LiveData<List<Academy>> = _academiesForSpinnerRepository

    fun requestGetClassroomById(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            classroomsRepository.getClassroomById(classroomId).let{response ->
                if(response.isSuccessful){
                    response.body().let{
                        _selectedClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetClassrooms(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            classroomsRepository.getClassroomsByTeacherId(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _getClassroomRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetAcademiesForNewClassroom(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            classroomsRepository.getAcademiesByTeacherId(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _academiesForSpinnerRepository.postValue(it)
                    }
                }
            }
        }
    }
}

class ManageClassroomViewModelFactory(private val classroomsRepository: ClassroomsRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ClassroomsRepository::class.java).newInstance(classroomsRepository)
    }
}