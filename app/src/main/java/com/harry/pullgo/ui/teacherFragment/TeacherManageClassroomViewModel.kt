package com.harry.pullgo.ui.teacherFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.repository.ClassroomsRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherManageClassroomViewModel @ViewModelInject constructor(
    private val classroomsRepository: ClassroomsRepository
    ): ViewModel() {
    private val _selectedClassroom = MutableLiveData<Resource<Classroom>>()
    val selectedClassroom: LiveData<Resource<Classroom>> = _selectedClassroom

    private val _getClassroomRepositories = MutableLiveData<Resource<List<Classroom>>>()
    val getClassroomRepositories: LiveData<Resource<List<Classroom>>> = _getClassroomRepositories

    private val _academiesForSpinnerRepository = MutableLiveData<Resource<List<Academy>>>()
    val academiesForSpinnerRepository: LiveData<Resource<List<Academy>>> = _academiesForSpinnerRepository

    fun requestGetClassroomById(classroomId: Long){
        _selectedClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            classroomsRepository.getClassroomById(classroomId).let{response ->
                if(response.isSuccessful){
                    _selectedClassroom.postValue(Resource.success(response.body()))
                }else{
                    _selectedClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetClassrooms(id: Long){
        _getClassroomRepositories.postValue(Resource.loading(null))

        viewModelScope.launch {
            classroomsRepository.getClassroomsByTeacherId(id).let{response ->
                if(response.isSuccessful){
                    _getClassroomRepositories.postValue(Resource.success(response.body()))
                }else{
                    _getClassroomRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetAcademiesForNewClassroom(id: Long){
        _academiesForSpinnerRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            classroomsRepository.getAcademiesByTeacherId(id).let{response ->
                if(response.isSuccessful){
                    _academiesForSpinnerRepository.postValue(Resource.success(response.body()))
                }else{
                    _academiesForSpinnerRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}