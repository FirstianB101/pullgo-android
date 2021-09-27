package com.harry.pullgo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.models.User
import com.harry.pullgo.data.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {
    private val _loginUserRepositories = MutableLiveData<User>()
    val loginUserRepositories: LiveData<User> = _loginUserRepositories

    private val _academyRepositoryStudentApplied = MutableLiveData<List<Academy>>()
    val academyRepositoryStudentApplied: LiveData<List<Academy>> = _academyRepositoryStudentApplied

    private val _academyRepositoryTeacherApplied = MutableLiveData<List<Academy>>()
    val academyRepositoryTeacherApplied: LiveData<List<Academy>> = _academyRepositoryTeacherApplied

    private val _loginMessage = MutableLiveData<String>()
    val loginMessage: LiveData<String> = _loginMessage

    fun requestLogin(account: Account){
        CoroutineScope(Dispatchers.IO).launch{
            loginRepository.getLoginUser(account).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _loginUserRepositories.postValue(it)
                    }
                }else{
                    _loginMessage.postValue("아이디 또는 비밀번호가 잘못되었습니다")
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