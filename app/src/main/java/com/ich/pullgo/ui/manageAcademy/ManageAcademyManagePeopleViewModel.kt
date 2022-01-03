package com.ich.pullgo.ui.manageAcademy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.data.repository.ManageAcademyRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAcademyManagePeopleViewModel @Inject constructor(
    private val manageAcademyRepository: ManageAcademyRepository
    ): ViewModel() {
    private val _teachersAtAcademyRepository = MutableLiveData<Resource<List<Teacher>>>()
    val teachersAtAcademyRepository: LiveData<Resource<List<Teacher>>> = _teachersAtAcademyRepository

    private val _studentsAtAcademyRepository = MutableLiveData<Resource<List<Student>>>()
    val studentsAtAcademyRepository: LiveData<Resource<List<Student>>> = _studentsAtAcademyRepository

    private val _kickPersonMessage = MutableLiveData<Resource<String>>()
    val kickPersonMessage: LiveData<Resource<String>> = _kickPersonMessage

    fun getTeachersAtAcademy(academyId: Long){
        _teachersAtAcademyRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.getTeachersSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    _teachersAtAcademyRepository.postValue(Resource.success(response.body()))
                }else{
                    _teachersAtAcademyRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getStudentsAtAcademy(academyId: Long){
        _studentsAtAcademyRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.getStudentsSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    _studentsAtAcademyRepository.postValue(Resource.success(response.body()))
                }else{
                    _studentsAtAcademyRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun kickStudent(academyId: Long, studentId: Long){
        _kickPersonMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.kickStudent(academyId, studentId).let { response ->
                if(response.isSuccessful){
                    _kickPersonMessage.postValue(Resource.success("학생을 제외했습니다"))
                }else{
                    _kickPersonMessage.postValue(Resource.error(response.code().toString(),"학생을 제외하지 못했습니다"))
                }
            }
        }
    }

    fun kickTeacher(academyId: Long, teacherId: Long){
        _kickPersonMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.kickTeacher(academyId, teacherId).let { response ->
                if(response.isSuccessful){
                    _kickPersonMessage.postValue(Resource.success("선생님을 제외했습니다"))
                }else{
                    _kickPersonMessage.postValue(Resource.error(response.code().toString(),"선생님을 제외하지 못했습니다"))
                }
            }
        }
    }
}