package com.harry.pullgo.ui.commonFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.models.User
import com.harry.pullgo.data.repository.ChangeInfoRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class ChangeInfoViewModel @ViewModelInject constructor(
    private val changeInfoRepository: ChangeInfoRepository
    ): ViewModel() {
    private val _changeStudent = MutableLiveData<Resource<Student>>()
    val changeStudent: LiveData<Resource<Student>> = _changeStudent

    private val _changeTeacher = MutableLiveData<Resource<Teacher>>()
    val changeTeacher: LiveData<Resource<Teacher>> = _changeTeacher

    private val _authUser = MutableLiveData<Resource<User>>()
    val authUser: LiveData<Resource<User>> = _authUser

    fun changeStudentInfo(studentId: Long, student: Student){
        _changeStudent.postValue(Resource.loading(null))

        viewModelScope.launch {
            changeInfoRepository.changeStudentInfo(studentId, student).let { response ->
                if(response.isSuccessful){
                    _changeStudent.postValue(Resource.success(response.body()))
                }else{
                    _changeStudent.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun changeTeacherInfo(teacherId: Long, teacher: Teacher){
        _changeTeacher.postValue(Resource.loading(null))

        viewModelScope.launch {
            changeInfoRepository.changeTeacherInfo(teacherId, teacher).let { response ->
                if(response.isSuccessful){
                    _changeTeacher.postValue(Resource.success(response.body()))
                }else{
                    _changeTeacher.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestAuth(account: Account){
        viewModelScope.launch{
            _authUser.postValue(Resource.loading(null))

            changeInfoRepository.authUser(account).let{ response ->
                if(response.isSuccessful){
                    _authUser.postValue(Resource.success(response.body()))
                }else{
                    _authUser.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}