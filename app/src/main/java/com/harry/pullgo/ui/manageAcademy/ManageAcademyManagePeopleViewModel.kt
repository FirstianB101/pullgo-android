package com.harry.pullgo.ui.manageAcademy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageAcademyManagePeopleViewModel(private val manageAcademyRepository: ManageAcademyRepository): ViewModel() {
    private val _teachersAtAcademyRepository = MutableLiveData<List<Teacher>>()
    val teachersAtAcademyRepository = _teachersAtAcademyRepository

    private val _studentsAtAcademyRepository = MutableLiveData<List<Student>>()
    val studentsAtAcademyRepository = _studentsAtAcademyRepository

    private val _kickPersonMessage = MutableLiveData<String>()
    val kickPersonMessage: LiveData<String> = _kickPersonMessage

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

    fun getStudentsAtAcademy(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            manageAcademyRepository.getStudentsSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsAtAcademyRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun kickStudent(academyId: Long, studentId: Long){
        manageAcademyRepository.kickStudent(academyId,studentId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _kickPersonMessage.postValue("학생을 제외했습니다")
                }else{
                    _kickPersonMessage.postValue("학생을 제외하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _kickPersonMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun kickTeacher(academyId: Long, teacherId: Long){
        manageAcademyRepository.kickTeacher(academyId,teacherId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _kickPersonMessage.postValue("선생님을 제외했습니다")
                }else{
                    _kickPersonMessage.postValue("선생님을 제외하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _kickPersonMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}

class ManageAcademyManagePeopleViewModelFactory(private val manageAcademyRepository: ManageAcademyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageAcademyRepository::class.java).newInstance(manageAcademyRepository)
    }
}