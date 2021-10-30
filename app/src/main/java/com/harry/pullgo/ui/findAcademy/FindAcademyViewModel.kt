package com.harry.pullgo.ui.findAcademy

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.repository.FindAcademyRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindAcademyViewModel @ViewModelInject constructor(
    private val findAcademyRepository: FindAcademyRepository
    ): ViewModel() {
    private val _findAcademyRepositories = MutableLiveData<Resource<List<Academy>>>()
    val findAcademyRepositories: LiveData<Resource<List<Academy>>> = _findAcademyRepositories

    private val _requestMessage = MutableLiveData<Resource<String>>()
    val requestMessage: LiveData<Resource<String>> = _requestMessage

    private val _createMessage = MutableLiveData<Resource<String>>()
    val createMessage: LiveData<Resource<String>> = _createMessage

    fun requestGetAcademies(name: String){
        _findAcademyRepositories.postValue(Resource.loading(null))

        viewModelScope.launch{
            findAcademyRepository.getAcademies(name).let{ response ->
                if(response.isSuccessful){
                    _findAcademyRepositories.postValue(Resource.success(response.body()))
                }else{
                    _findAcademyRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestStudentApply(studentId: Long, academyId: Long){
        _requestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            findAcademyRepository.requestStudentApply(studentId, academyId).let{ response ->
                if(response.isSuccessful){
                    _requestMessage.postValue(Resource.success("가입 요청이 완료되었습니다"))
                }else{
                    _requestMessage.postValue(Resource.error(response.code().toString(),"가입 요청에 실패했습니다"))
                }
            }
        }
    }

    fun requestTeacherApply(teacherId: Long, academyId: Long){
        _requestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            findAcademyRepository.requestTeacherApply(teacherId, academyId).let{ response ->
                if(response.isSuccessful){
                    _requestMessage.postValue(Resource.success("가입 요청이 완료되었습니다"))
                }else{
                    _requestMessage.postValue(Resource.error(response.code().toString(),"가입 요청에 실패했습니다"))
                }
            }
        }
    }

    fun createAcademy(academy: Academy){
        _createMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            findAcademyRepository.createAcademy(academy).let{ response ->
                if(response.isSuccessful){
                    _createMessage.postValue(Resource.success("학원을 생성하였습니다"))
                }else{
                    _createMessage.postValue(Resource.error(response.code().toString(),"학원을 생성하지 못했습니다"))
                }
            }
        }
    }
}