package com.harry.pullgo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher
import com.harry.pullgo.data.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {
    private val _loginStudentRepositories = MutableLiveData<Student>()
    val loginStudentRepositories=_loginStudentRepositories

    private val _loginTeacherRepositories = MutableLiveData<Teacher>()
    val loginTeacherRepositories=_loginTeacherRepositories

    fun requestStudentLogin(id: Long){
        CoroutineScope(Dispatchers.Main).launch{
            loginRepository.getLoginStudent(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _loginStudentRepositories.setValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherLogin(id: Long){
        CoroutineScope(Dispatchers.Main).launch{
            loginRepository.getLoginTeacher(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _loginTeacherRepositories.setValue(it)
                    }
                }
            }
        }
    }
}

class LoginViewModelFactory(private val loginRepository: LoginRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LoginRepository::class.java).newInstance(loginRepository)
    }
}