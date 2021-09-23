package com.harry.pullgo.ui.findAcademy

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.FindAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindAcademyViewModel(private val findAcademyRepository: FindAcademyRepository): ViewModel() {
    private val _findAcademyRepositories = MutableLiveData<List<Academy>>()
    val findAcademyRepositories = _findAcademyRepositories

    private val _requestMessage = MutableLiveData<String>()
    val requestMessage = _requestMessage

    private val _createMessage = MutableLiveData<String>()
    val createMessage = _createMessage

    fun requestGetAcademies(name: String){
        CoroutineScope(Dispatchers.IO).launch{
            findAcademyRepository.getAcademies(name).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _findAcademyRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun requestStudentApply(studentId: Long, academyId: Long){
        findAcademyRepository.requestStudentApply(studentId,academyId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _requestMessage.postValue("가입 요청이 완료되었습니다")
                }else{
                    _requestMessage.postValue("이미 해당 학원에 등록되어 있습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _requestMessage.postValue("서버 연결에 실패했습니다")
            }
        })
    }

    fun requestTeacherApply(teacherId: Long, academyId: Long){
        findAcademyRepository.requestTeacherApply(teacherId,academyId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _requestMessage.postValue("가입 요청이 완료되었습니다")
                }else{
                    _requestMessage.postValue("이미 해당 학원에 등록되어 있습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _requestMessage.postValue("서버 연결에 실패했습니다")
            }
        })
    }

    fun createAcademy(academy: Academy){
        findAcademyRepository.createAcademy(academy).enqueue(object: Callback<Academy> {
            override fun onResponse(call: Call<Academy>, response: Response<Academy>) {
                if(response.isSuccessful){
                    _createMessage.postValue("학원을 생성하였습니다")
                }else{
                    _createMessage.postValue("학원을 생성하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Academy>, t: Throwable) {
                _createMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}

class FindAcademyViewModelFactory(private val findAcademyRepository: FindAcademyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FindAcademyRepository::class.java).newInstance(findAcademyRepository)
    }
}