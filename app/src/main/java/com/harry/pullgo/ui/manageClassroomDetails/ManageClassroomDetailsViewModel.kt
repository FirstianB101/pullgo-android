package com.harry.pullgo.ui.manageClassroomDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.ClassroomsRepository
import com.harry.pullgo.data.repository.ManageClassroomDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageClassroomDetailsViewModel(private val detailsRepository: ManageClassroomDetailsRepository): ViewModel() {
    private val _studentsAppliedClassroom = MutableLiveData<List<Student>>()
    val studentsAppliedClassroom = _studentsAppliedClassroom

    private val _teachersAppliedClassroom = MutableLiveData<List<Teacher>>()
    val teachersAppliedClassroom = _teachersAppliedClassroom

    private val _studentsRequestApplyClassroom = MutableLiveData<List<Student>>()
    val studentsRequestApplyClassroom = _studentsRequestApplyClassroom

    private val _teachersRequestApplyClassroom = MutableLiveData<List<Teacher>>()
    val teachersRequestApplyClassroom = _teachersRequestApplyClassroom

    fun requestGetStudentsAppliedClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            detailsRepository.getStudentsAppliedClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsAppliedClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetTeachersAppliedClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            detailsRepository.getTeachersAppliedClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersAppliedClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetTeachersRequestApplyClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            detailsRepository.getTeachersRequestApplyClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersRequestApplyClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetStudentsRequestApplyClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            detailsRepository.getStudentsRequestApplyClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsRequestApplyClassroom.postValue(it)
                    }
                }
            }
        }
    }
}

class ManageClassroomDetailsViewModelFactory(private val detailsRepository: ManageClassroomDetailsRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageClassroomDetailsRepository::class.java).newInstance(detailsRepository)
    }
}