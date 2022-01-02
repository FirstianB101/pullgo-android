package com.ich.pullgo.ui.applyClassroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.data.models.Academy
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.repository.ApplyClassroomRepository
import com.ich.pullgo.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyClassroomViewModel @Inject constructor(
    private val applyClassroomRepository: ApplyClassroomRepository
    ): ViewModel() {
    private val _appliedAcademiesRepository = MutableLiveData<Resource<List<Academy>>>()
    val appliedAcademiesRepository: LiveData<Resource<List<Academy>>> = _appliedAcademiesRepository

    private val _applyClassroomsRepositories = MutableLiveData<Resource<List<Classroom>>>()
    val applyClassroomsRepositories: LiveData<Resource<List<Classroom>>> = _applyClassroomsRepositories

    private val _appliedClassroomsMessage = MutableLiveData<Resource<String>>()
    val appliedClassroomsMessage: LiveData<Resource<String>> = _appliedClassroomsMessage

    fun requestStudentAppliedAcademies(id: Long){
        _appliedAcademiesRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            applyClassroomRepository.getAcademiesStudentApplied(id).let { response ->
                if(response.isSuccessful){
                    _appliedAcademiesRepository.postValue(Resource.success(response.body()))
                }else{
                    _appliedAcademiesRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestTeacherAppliedAcademies(id: Long){
        _appliedAcademiesRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            applyClassroomRepository.getAcademiesTeacherApplied(id).let { response ->
                if(response.isSuccessful){
                    _appliedAcademiesRepository.postValue(Resource.success(response.body()))
                }else{
                    _appliedAcademiesRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetClassrooms(academyId: Long, name: String){
        _applyClassroomsRepositories.postValue(Resource.loading(null))

        viewModelScope.launch {
            applyClassroomRepository.getClassroomsByNameAndAcademyID(academyId,name).let{ response ->
                if(response.isSuccessful){
                    if(response.isSuccessful){
                        _applyClassroomsRepositories.postValue(Resource.success(response.body()))
                    }else{
                        _applyClassroomsRepositories.postValue(Resource.error(response.code().toString(),null))
                    }
                }
            }
        }
    }

    fun resetClassroomSearchResult(){
        _applyClassroomsRepositories.postValue(Resource.success(null))
    }

    fun requestStudentApplyClassroom(studentId: Long,selectedClassroom: Classroom?){
        _appliedClassroomsMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            applyClassroomRepository.studentApplyClassroom(studentId, selectedClassroom?.id!!).let { response ->
                if(response.isSuccessful){
                    _appliedClassroomsMessage.postValue(Resource.success("가입 요청이 성공하였습니다"))
                }else{
                    _appliedClassroomsMessage.postValue(Resource.error(response.code().toString(),"가입 요청에 실패했습니다"))
                }
            }
        }
    }

    fun requestTeacherApplyClassroom(teacherId: Long, selectedClassroom: Classroom?){
        _appliedClassroomsMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            applyClassroomRepository.teacherApplyClassroom(teacherId, selectedClassroom?.id!!).let { response ->
                if(response.isSuccessful){
                    _appliedClassroomsMessage.postValue(Resource.success("가입 요청이 성공하였습니다"))
                }else{
                    _appliedClassroomsMessage.postValue(Resource.error(response.code().toString(),"가입 요청에 실패했습니다"))
                }
            }
        }
    }
}