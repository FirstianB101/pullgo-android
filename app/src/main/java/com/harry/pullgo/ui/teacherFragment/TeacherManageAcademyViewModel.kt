package com.harry.pullgo.ui.teacherFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.ClassroomsRepository
import com.harry.pullgo.data.repository.ManageAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherManageAcademyViewModel(private val manageAcademyRepository: ManageAcademyRepository): ViewModel() {
    private val _ownedAcademiesRepository = MutableLiveData<List<Academy>>()
    val ownedAcademiesRepository = _ownedAcademiesRepository

    private val _teachersAtAcademyRepository = MutableLiveData<List<Teacher>>()
    val teachersAtAcademyRepository = _teachersAtAcademyRepository

    fun requestGetOwnedAcademies(ownerId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            manageAcademyRepository.getAcademiesByOwnerId(ownerId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _ownedAcademiesRepository.postValue(it)
                    }
                }
            }
        }
    }

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
}


class TeacherManageAcademyViewModelFactory(private val manageAcademyRepository: ManageAcademyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageAcademyRepository::class.java).newInstance(manageAcademyRepository)
    }
}