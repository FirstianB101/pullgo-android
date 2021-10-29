package com.harry.pullgo.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Account
import com.harry.pullgo.data.models.User
import com.harry.pullgo.data.repository.LoginRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val loginRepository: LoginRepository
    ): ViewModel() {
    private val _loginUserRepositories = MutableLiveData<Resource<User>>()
    val loginUserRepositories: LiveData<Resource<User>> = _loginUserRepositories

    private val _academyRepositoryStudentApplied = MutableLiveData<Resource<List<Academy>>>()
    val academyRepositoryStudentApplied: LiveData<Resource<List<Academy>>> = _academyRepositoryStudentApplied

    private val _academyRepositoryTeacherApplied = MutableLiveData<Resource<List<Academy>>>()
    val academyRepositoryTeacherApplied: LiveData<Resource<List<Academy>>> = _academyRepositoryTeacherApplied


    fun requestLogin(account: Account){
        viewModelScope.launch{
            _loginUserRepositories.postValue(Resource.loading(null))

            loginRepository.getLoginUser(account).let{ response ->
                if(response.isSuccessful){
                    _loginUserRepositories.postValue(Resource.success(response.body()))
                }else{
                    _loginUserRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestAuthorize(){
        viewModelScope.launch{
            _loginUserRepositories.postValue(Resource.loading(null))

            loginRepository.getAutoLoginUser().let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _loginUserRepositories.postValue(Resource.success(it))
                    }
                }
            }
        }
    }

    fun requestStudentAcademies(id: Long){
        viewModelScope.launch{
            _academyRepositoryStudentApplied.postValue(Resource.loading(null))

            loginRepository.getAcademiesByStudentId(id).let{ response ->
                if(response.isSuccessful){
                    _academyRepositoryStudentApplied.postValue(Resource.success(response.body()))
                }else{
                    _academyRepositoryStudentApplied.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestTeacherAcademies(id: Long){
        viewModelScope.launch{
            _academyRepositoryTeacherApplied.postValue(Resource.loading(null))

            loginRepository.getAcademiesByTeacherId(id).let{ response ->
                if(response.isSuccessful){
                    _academyRepositoryTeacherApplied.postValue(Resource.success(response.body()))
                }else{
                    _academyRepositoryTeacherApplied.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}