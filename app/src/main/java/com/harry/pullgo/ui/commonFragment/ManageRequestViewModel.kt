package com.harry.pullgo.ui.commonFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.repository.ManageAcademyRepository
import com.harry.pullgo.data.repository.ManageRequestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageRequestViewModel(private val repository: ManageRequestRepository): ViewModel() {
    private val _applyingAcademyRepository = MutableLiveData<List<Academy>>()
    val applyingAcademyRepository: LiveData<List<Academy>> = _applyingAcademyRepository

    private val _applyingClassroomRepository = MutableLiveData<List<Classroom>>()
    val applyingClassroomRepository: LiveData<List<Classroom>> = _applyingClassroomRepository

    private val _removeRequestMessage = MutableLiveData<String>()
    val removeRequestMessage: LiveData<String> = _removeRequestMessage

    fun getStudentApplyingAcademy(studentId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getStudentApplyingAcademies(studentId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _applyingAcademyRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun getStudentApplyingClassroom(studentId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getStudentApplyingClassrooms(studentId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _applyingClassroomRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun getTeacherApplyingAcademy(teacherId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getTeacherApplyingAcademies(teacherId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _applyingAcademyRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun getTeacherApplyingClassroom(teacherId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getTeacherApplyingClassrooms(teacherId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _applyingClassroomRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun removeStudentApplyingAcademy(studentId: Long, academyId: Long){
        repository.removeStudentAppliedAcademy(studentId, academyId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _removeRequestMessage.postValue("학원 가입 요청을 제거했습니다")
                }else{
                    _removeRequestMessage.postValue("학원 가입 요청을 제거하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _removeRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun removeStudentApplyingClassroom(studentId: Long, classroomId: Long){
        repository.removeStudentAppliedClassroom(studentId, classroomId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _removeRequestMessage.postValue("반 가입 요청을 제거했습니다")
                }else{
                    _removeRequestMessage.postValue("반 가입 요청을 제거하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _removeRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun removeTeacherApplyingAcademy(teacherId: Long, academyId: Long){
        repository.removeTeacherAppliedAcademy(teacherId, academyId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _removeRequestMessage.postValue("학원 가입 요청을 제거했습니다")
                }else{
                    _removeRequestMessage.postValue("학원 가입 요청을 제거하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _removeRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun removeTeacherApplyingClassroom(teacherId: Long, classroomId: Long){
        repository.removeTeacherAppliedClassroom(teacherId, classroomId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _removeRequestMessage.postValue("반 가입 요청을 제거했습니다")
                }else{
                    _removeRequestMessage.postValue("반 가입 요청을 제거하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _removeRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}


class ManageRequestViewModelFactory(private val manageRequestRepository: ManageRequestRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageRequestRepository::class.java).newInstance(manageRequestRepository)
    }
}