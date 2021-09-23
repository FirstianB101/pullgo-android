package com.harry.pullgo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {
    private val _loginStudentRepositories = MutableLiveData<Student>()
    val loginStudentRepositories=_loginStudentRepositories

    private val _academyRepositoryStudentApplied = MutableLiveData<List<Academy>>()
    val academyRepositoryStudentApplied = _academyRepositoryStudentApplied

    private val _loginTeacherRepositories = MutableLiveData<Teacher>()
    val loginTeacherRepositories = _loginTeacherRepositories

    private val _academyRepositoryTeacherApplied = MutableLiveData<List<Academy>>()
    val academyRepositoryTeacherApplied = _academyRepositoryTeacherApplied

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

    fun requestStudentAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch{
            loginRepository.getAcademiesByStudentId(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _academyRepositoryStudentApplied.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch{
            loginRepository.getAcademiesByTeacherId(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _academyRepositoryTeacherApplied.postValue(it)
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