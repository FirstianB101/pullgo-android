package com.harry.pullgo.ui.signUp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Exist
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.SignUpRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel @ViewModelInject constructor(
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

    private val _usernameExist = MutableLiveData<Exist>()
    val usernameExist: LiveData<Exist> = _usernameExist

    private val _createMessage = MutableLiveData<String>()
    val createMessage: LiveData<String> = _createMessage

    private val _existsMessage = MutableLiveData<String>()
    val existsMessage: LiveData<String> = _existsMessage

    fun createStudent(student: Student){
        signUpRepository.createStudent(student).enqueue(object: Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if(response.isSuccessful){
                    _createMessage.postValue("계정이 생성되었습니다")
                }else{
                    _createMessage.postValue("계정을 생성하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                _createMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun createTeacher(teacher: Teacher){
        signUpRepository.createTeacher(teacher).enqueue(object: Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                if(response.isSuccessful){
                    _createMessage.postValue("계정이 생성되었습니다")
                }else{
                    _createMessage.postValue("계정을 생성하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
                _createMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun studentUsernameExists(username: String){
        CoroutineScope(Dispatchers.IO).launch {
            signUpRepository.studentUsernameExists(username).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                        _usernameExist.postValue(it)
                    }
                }
            }
        }
    }

    fun teacherUsernameExists(username: String){
        CoroutineScope(Dispatchers.IO).launch {
            signUpRepository.teacherUsernameExists(username).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                       _usernameExist.postValue(it)
                    }
                }
            }
        }
    }
}