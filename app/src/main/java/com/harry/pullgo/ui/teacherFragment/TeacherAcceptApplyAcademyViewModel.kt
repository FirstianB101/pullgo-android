package com.harry.pullgo.ui.teacherFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.AcceptApplyAcademyRepository
import com.harry.pullgo.data.repository.FindAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeacherAcceptApplyAcademyViewModel(private val acceptApplyAcademyRepository: AcceptApplyAcademyRepository):ViewModel() {
    private val _studentsAppliedAcademy = MutableLiveData<List<Student>>()
    val studentsAppliedAcademy = _studentsAppliedAcademy

    private val _teachersAppliedAcademy = MutableLiveData<List<Teacher>>()
    val teacherAppliedAcademy = _teachersAppliedAcademy

    private val _academyRepositories = MutableLiveData<List<Academy>>()
    val academyRepositories = _academyRepositories

    fun requestGetStudents(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch{
            acceptApplyAcademyRepository.getStudentsAppliedAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsAppliedAcademy.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetTeachers(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch{
            acceptApplyAcademyRepository.getTeachersAppliedAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersAppliedAcademy.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherAcademies(teacherId: Long){
        CoroutineScope(Dispatchers.IO).launch{
            acceptApplyAcademyRepository.getTeachersAcademies(teacherId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _academyRepositories.postValue(it)
                    }
                }
            }
        }
    }
}

class TeacherAcceptApplyAcademyViewModelFactory(private val acceptApplyAcademyRepository: AcceptApplyAcademyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AcceptApplyAcademyRepository::class.java).newInstance(acceptApplyAcademyRepository)
    }
}