package com.ich.pullgo.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.data.remote.dto.toExist
import com.ich.pullgo.data.repository.SignUpRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.Exist
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
    ): ViewModel() {
    private val _signUpId = MutableLiveData<String>()
    val signUpId = _signUpId

    private val _signUpPw = MutableLiveData<String>()
    val signUpPw = _signUpPw

    private val _signUpStudent = MutableLiveData<Student>()
    val signUpStudent = _signUpStudent

    private val _signUpTeacher = MutableLiveData<Teacher>()
    val signUpTeacher = _signUpTeacher

    private val _usernameExist = MutableLiveData<Resource<Exist>>()
    val usernameExist: LiveData<Resource<Exist>> = _usernameExist

    private val _createMessage = MutableLiveData<Resource<String>>()
    val createMessage: LiveData<Resource<String>> = _createMessage

    fun createStudent(student: Student){
        _createMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            signUpRepository.createStudent(student).let{response ->
                if(response.isSuccessful){
                    _createMessage.postValue(Resource.success("계정이 생성되었습니다"))
                }else{
                    _createMessage.postValue(Resource.error(response.code().toString(),"계정을 생성하지 못했습니다"))
                }
            }
        }
    }

    fun createTeacher(teacher: Teacher){
        _createMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            signUpRepository.createTeacher(teacher).let{response ->
                if(response.isSuccessful){
                    _createMessage.postValue(Resource.success("계정이 생성되었습니다"))
                }else{
                    _createMessage.postValue(Resource.error(response.code().toString(),"계정을 생성하지 못했습니다"))
                }
            }
        }
    }

    fun studentUsernameExists(username: String){
        _usernameExist.postValue(Resource.loading(null))

        viewModelScope.launch {
            signUpRepository.studentUsernameExists(username).let { response ->
                if(response.isSuccessful){
                    _usernameExist.postValue(Resource.success(response.body()?.toExist()))
                }else{
                    _usernameExist.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun teacherUsernameExists(username: String){
        _usernameExist.postValue(Resource.loading(null))

        viewModelScope.launch {
            signUpRepository.teacherUsernameExists(username).let { response ->
                if(response.isSuccessful){
                    if(response.isSuccessful){
                        _usernameExist.postValue(Resource.success(response.body()?.toExist()))
                    }else{
                        _usernameExist.postValue(Resource.error(response.code().toString(),null))
                    }
                }
            }
        }
    }
}