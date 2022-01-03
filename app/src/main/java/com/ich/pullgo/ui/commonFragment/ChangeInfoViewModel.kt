package com.ich.pullgo.ui.commonFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/commonFragment/ChangeInfoViewModel.kt
import com.ich.pullgo.data.models.Account
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.models.Teacher
import com.ich.pullgo.data.models.User
import com.ich.pullgo.data.repository.ChangeInfoRepository
import com.ich.pullgo.data.utils.Resource
=======
import com.ich.pullgo.data.repository.ChangeInfoRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/commonFragment/ChangeInfoViewModel.kt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeInfoViewModel @Inject constructor(
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