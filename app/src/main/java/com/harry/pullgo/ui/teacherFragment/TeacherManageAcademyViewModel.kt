package com.harry.pullgo.ui.teacherFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherManageAcademyViewModel @ViewModelInject constructor(
    private val manageAcademyRepository: ManageAcademyRepository
    ): ViewModel() {
    private val _ownedAcademiesRepository = MutableLiveData<List<Academy>>()
    val ownedAcademiesRepository: LiveData<List<Academy>> = _ownedAcademiesRepository

    private val _teachersAtAcademyRepository = MutableLiveData<List<Teacher>>()
    val teachersAtAcademyRepository: LiveData<List<Teacher>> = _teachersAtAcademyRepository

    private val _manageAcademyMessage = MutableLiveData<String>()
    val manageAcademyMessage: LiveData<String> = _manageAcademyMessage

    fun requestGetOwnedAcademies(ownerId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            manageAcademyRepository.getAcademiesByOwnerId(ownerId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _ownedAcademiesRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun getTeachersAtAcademy(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            manageAcademyRepository.getTeachersSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersAtAcademyRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun editAcademy(academyId: Long, academy: Academy){
        manageAcademyRepository.editAcademy(academyId, academy).enqueue(object: Callback<Academy> {
            override fun onResponse(call: Call<Academy>, response: Response<Academy>) {
                if(response.isSuccessful){
                    _manageAcademyMessage.postValue("수정되었습니다")
                }else{
                    _manageAcademyMessage.postValue("수정하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Academy>, t: Throwable) {
                _manageAcademyMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun deleteAcademy(academyId: Long){
        manageAcademyRepository.deleteAcademy(academyId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _manageAcademyMessage.postValue("학원이 삭제되었습니다")
                }else{
                    _manageAcademyMessage.postValue("학원을 삭제하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _manageAcademyMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}