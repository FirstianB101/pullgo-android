package com.harry.pullgo.ui.applyClassroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.objects.LoginInfo
import com.harry.pullgo.data.repository.ApplyClassroomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyClassroomViewModel(private val applyClassroomRepository: ApplyClassroomRepository): ViewModel() {
    private val _appliedAcademiesRepository = MutableLiveData<List<Academy>>()
    val appliedAcademiesRepository: LiveData<List<Academy>> = _appliedAcademiesRepository

    private val _applyClassroomsRepositories = MutableLiveData<List<Classroom>>()
    val applyClassroomsRepositories: LiveData<List<Classroom>> = _applyClassroomsRepositories

    private val _appliedClassroomsMessage = MutableLiveData<String>()
    val appliedClassroomsMessage: LiveData<String> = _appliedClassroomsMessage

    fun requestStudentAppliedAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            applyClassroomRepository.getAcademiesStudentApplied(id).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                        _appliedAcademiesRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherAppliedAcademies(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            applyClassroomRepository.getAcademiesTeacherApplied(id).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                        _appliedAcademiesRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetClassrooms(academyId: Long, name: String){
        CoroutineScope(Dispatchers.IO).launch {
            applyClassroomRepository.getClassroomsByNameAndAcademyID(academyId,name).let{ response ->
                if(response.isSuccessful){
                    if(response.isSuccessful){
                        response.body().let{
                            _applyClassroomsRepositories.postValue(it)
                        }
                    }
                }
            }
        }
    }

    fun resetClassroomSearchResult(){
        _applyClassroomsRepositories.postValue(null)
    }

    fun requestStudentApplyClassroom(selectedClassroom: Classroom?){
        applyClassroomRepository.studentApplyClassroom(LoginInfo.loginStudent?.id!!, selectedClassroom?.id!!)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        _appliedClassroomsMessage.postValue("가입 요청이 성공하였습니다")
                    }else{
                        _appliedClassroomsMessage.postValue("이미 가입된 반입니다")
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    _appliedClassroomsMessage.postValue("서버와 연결에 실패하였습니다")
                }
            })
    }

    fun requestTeacherApplyClassroom(selectedClassroom: Classroom?){
        applyClassroomRepository.teacherApplyClassroom(LoginInfo.loginTeacher?.id!!, selectedClassroom?.id!!)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        _appliedClassroomsMessage.postValue("가입 요청이 성공하였습니다")
                    }else{
                        _appliedClassroomsMessage.postValue("이미 가입된 반입니다")
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    _appliedClassroomsMessage.postValue("서버와 연결에 실패하였습니다")
                }
            })
    }
}

class ApplyClassroomViewModelFactory(private val applyClassroomRepository: ApplyClassroomRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ApplyClassroomRepository::class.java).newInstance(applyClassroomRepository)
    }
}