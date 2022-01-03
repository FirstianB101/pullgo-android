package com.ich.pullgo.ui.commonFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/commonFragment/ManageRequestViewModel.kt
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.repository.ManageRequestRepository
import com.ich.pullgo.data.utils.Resource
=======
import com.ich.pullgo.data.repository.ManageRequestRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/commonFragment/ManageRequestViewModel.kt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageRequestViewModel @Inject constructor(
    private val repository: ManageRequestRepository
    ): ViewModel() {
    private val _applyingAcademyRepository = MutableLiveData<Resource<List<Academy>>>()
    val applyingAcademyRepository: LiveData<Resource<List<Academy>>> = _applyingAcademyRepository

    private val _applyingClassroomRepository = MutableLiveData<Resource<List<Classroom>>>()
    val applyingClassroomRepository: LiveData<Resource<List<Classroom>>> = _applyingClassroomRepository

    private val _removeRequestMessage = MutableLiveData<Resource<String>>()
    val removeRequestMessage: LiveData<Resource<String>> = _removeRequestMessage

    fun getStudentApplyingAcademy(studentId: Long){
        _applyingAcademyRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getStudentApplyingAcademies(studentId).let{ response ->
                if(response.isSuccessful){
                    _applyingAcademyRepository.postValue(Resource.success(response.body()))
                }else{
                    _applyingAcademyRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getStudentApplyingClassroom(studentId: Long){
        _applyingClassroomRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getStudentApplyingClassrooms(studentId).let{ response ->
                if(response.isSuccessful){
                    _applyingClassroomRepository.postValue(Resource.success(response.body()))
                }else{
                    _applyingClassroomRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getTeacherApplyingAcademy(teacherId: Long){
        _applyingAcademyRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getTeacherApplyingAcademies(teacherId).let{ response ->
                if(response.isSuccessful){
                    _applyingAcademyRepository.postValue(Resource.success(response.body()))
                }else{
                    _applyingAcademyRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getTeacherApplyingClassroom(teacherId: Long){
        _applyingClassroomRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getTeacherApplyingClassrooms(teacherId).let{ response ->
                if(response.isSuccessful){
                    _applyingClassroomRepository.postValue(Resource.success(response.body()))
                }else{
                    _applyingClassroomRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun removeStudentApplyingAcademy(studentId: Long, academyId: Long){
        _removeRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.removeStudentAppliedAcademy(studentId, academyId).let{ response ->
                if(response.isSuccessful){
                    _removeRequestMessage.postValue(Resource.success("학원 가입 요청을 제거했습니다"))
                }else{
                    _removeRequestMessage.postValue(Resource.error(response.code().toString(),"학원 가입 요청을 제거하지 못했습니다"))
                }
            }
        }
    }

    fun removeStudentApplyingClassroom(studentId: Long, classroomId: Long){
        _removeRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.removeStudentAppliedClassroom(studentId, classroomId).let{ response ->
                if(response.isSuccessful){
                    _removeRequestMessage.postValue(Resource.success("반 가입 요청을 제거했습니다"))
                }else{
                    _removeRequestMessage.postValue(Resource.error(response.code().toString(),"반 가입 요청을 제거하지 못했습니다"))
                }
            }
        }
    }

    fun removeTeacherApplyingAcademy(teacherId: Long, academyId: Long){
        _removeRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.removeTeacherAppliedAcademy(teacherId, academyId).let{ response ->
                if(response.isSuccessful){
                    _removeRequestMessage.postValue(Resource.success("학원 가입 요청을 제거했습니다"))
                }else{
                    _removeRequestMessage.postValue(Resource.error(response.code().toString(),"학원 가입 요청을 제거하지 못했습니다"))
                }
            }
        }
    }

    fun removeTeacherApplyingClassroom(teacherId: Long, classroomId: Long){
        _removeRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.removeTeacherAppliedClassroom(teacherId, classroomId).let{ response ->
                if(response.isSuccessful){
                    _removeRequestMessage.postValue(Resource.success("반 가입 요청을 제거했습니다"))
                }else{
                    _removeRequestMessage.postValue(Resource.error(response.code().toString(),"반 가입 요청을 제거하지 못했습니다"))
                }
            }
        }
    }
}