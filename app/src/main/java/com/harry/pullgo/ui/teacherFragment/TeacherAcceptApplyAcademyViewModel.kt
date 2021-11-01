package com.harry.pullgo.ui.teacherFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.AcceptApplyAcademyRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherAcceptApplyAcademyViewModel @ViewModelInject constructor(
    private val acceptApplyAcademyRepository: AcceptApplyAcademyRepository
    ):ViewModel() {
    private val _studentsAppliedAcademy = MutableLiveData<Resource<List<Student>>>()
    val studentsAppliedAcademy: LiveData<Resource<List<Student>>> = _studentsAppliedAcademy

    private val _teachersAppliedAcademy = MutableLiveData<Resource<List<Teacher>>>()
    val teacherAppliedAcademy: LiveData<Resource<List<Teacher>>> = _teachersAppliedAcademy

    private val _academyRepositories = MutableLiveData<Resource<List<Academy>>>()
    val academyRepositories: LiveData<Resource<List<Academy>>> = _academyRepositories

    private val _acceptOrDenyMessage = MutableLiveData<Resource<String>>()
    val acceptOrDenyMessage: LiveData<Resource<String>> = _acceptOrDenyMessage


    fun requestGetStudents(academyId: Long){
        _studentsAppliedAcademy.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.getStudentsAppliedAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    _studentsAppliedAcademy.postValue(Resource.success(response.body()))
                }else{
                    _studentsAppliedAcademy.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetTeachers(academyId: Long){
        _teachersAppliedAcademy.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.getTeachersAppliedAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    _teachersAppliedAcademy.postValue(Resource.success(response.body()))
                }else{
                    _teachersAppliedAcademy.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestTeacherAcademies(teacherId: Long){
        _academyRepositories.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.getTeachersAcademies(teacherId).let{ response ->
                if(response.isSuccessful){
                    _academyRepositories.postValue(Resource.success(response.body()))
                }else{
                    _academyRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun acceptStudentApplyAcademy(academyId: Long, studentId: Long){
        _acceptOrDenyMessage.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.acceptStudentApply(academyId, studentId).let{ response ->
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue(Resource.success("해당 학생의 요청을 승인하였습니다"))
                }else{
                    _acceptOrDenyMessage.postValue(Resource.error(response.code().toString(),"해당 학생의 요청을 승인하였습니다"))
                }
            }
        }
    }

    fun acceptTeacherApplyAcademy(academyId: Long, teacherId: Long){
        _acceptOrDenyMessage.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.acceptTeacherApply(academyId, teacherId).let{ response ->
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue(Resource.success("해당 선생님의 요청을 승인하였습니다"))
                }else{
                    _acceptOrDenyMessage.postValue(Resource.error(response.code().toString(),"해당 선생님의 요청을 승인하였습니다"))
                }
            }
        }
    }

    fun denyStudentApplyAcademy(academyId: Long, studentId: Long){
        _acceptOrDenyMessage.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.denyStudentApply(studentId, academyId).let{ response ->
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue(Resource.success("해당 학생의 요청이 삭제되었습니다"))
                }else{
                    _acceptOrDenyMessage.postValue(Resource.error(response.code().toString(),"해당 학생의 요청을 삭제하지 못했습니다"))
                }
            }
        }
    }
    fun denyTeacherApplyAcademy(academyId: Long, teacherId: Long){
        _acceptOrDenyMessage.postValue(Resource.loading(null))

        viewModelScope.launch{
            acceptApplyAcademyRepository.denyTeacherApply(teacherId, academyId).let{ response ->
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue(Resource.success("해당 선생님의 요청이 삭제되었습니다"))
                }else{
                    _acceptOrDenyMessage.postValue(Resource.error(response.code().toString(),"해당 선생님의 요청을 삭제하지 못했습니다"))
                }
            }
        }
    }
}