package com.harry.pullgo.ui.manageAcademy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.ManageAcademyRepository
import com.harry.pullgo.data.repository.ManageClassroomDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageAcademyManagePeopleViewModel(private val manageAcademyRepository: ManageAcademyRepository): ViewModel() {
    private val _teachersAtAcademyRepository = MutableLiveData<List<Teacher>>()
    val teachersAtAcademyRepository = _teachersAtAcademyRepository

    private val _studentsAtAcademyRepository = MutableLiveData<List<Student>>()
    val studentsAtAcademyRepository = _studentsAtAcademyRepository

    fun getTeachersAtAcademy(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            manageAcademyRepository.getTeachersSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersAtAcademyRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun getStudentsAtAcademy(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            manageAcademyRepository.getStudentsSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsAtAcademyRepository.postValue(it)
                    }
                }
            }
        }
    }
}

class ManageAcademyManagePeopleViewModelFactory(private val manageAcademyRepository: ManageAcademyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageAcademyRepository::class.java).newInstance(manageAcademyRepository)
    }
}