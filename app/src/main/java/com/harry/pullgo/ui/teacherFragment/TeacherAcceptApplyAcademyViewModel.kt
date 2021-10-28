package com.harry.pullgo.ui.teacherFragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.AcceptApplyAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherAcceptApplyAcademyViewModel @ViewModelInject constructor(
    private val acceptApplyAcademyRepository: AcceptApplyAcademyRepository
    ):ViewModel() {
    private val _studentsAppliedAcademy = MutableLiveData<List<Student>>()
    val studentsAppliedAcademy: LiveData<List<Student>> = _studentsAppliedAcademy

    private val _teachersAppliedAcademy = MutableLiveData<List<Teacher>>()
    val teacherAppliedAcademy: LiveData<List<Teacher>> = _teachersAppliedAcademy

    private val _academyRepositories = MutableLiveData<List<Academy>>()
    val academyRepositories: LiveData<List<Academy>> = _academyRepositories

    private val _acceptOrDenyMessage = MutableLiveData<String>()
    val acceptOrDenyMessage: LiveData<String> = _acceptOrDenyMessage

    fun requestGetStudents(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch{
            acceptApplyAcademyRepository.getStudentsAppliedAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsAppliedAcademy.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetTeachers(academyId: Long){
        CoroutineScope(Dispatchers.IO).launch{
            acceptApplyAcademyRepository.getTeachersAppliedAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersAppliedAcademy.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherAcademies(teacherId: Long){
        CoroutineScope(Dispatchers.IO).launch{
            acceptApplyAcademyRepository.getTeachersAcademies(teacherId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _academyRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun acceptStudentApplyAcademy(academyId: Long, studentId: Long){
        acceptApplyAcademyRepository.acceptStudentApply(academyId, studentId).enqueue(object:
            Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue("해당 학생의 요청을 승인하였습니다")
                }else{
                    _acceptOrDenyMessage.postValue("해당 학생의 요청 승인에 실패하였습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _acceptOrDenyMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun acceptTeacherApplyAcademy(academyId: Long, teacherId: Long){
        acceptApplyAcademyRepository.acceptTeacherApply(academyId, teacherId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue("해당 선생님의 요청을 승인하였습니다")
                }else{
                    _acceptOrDenyMessage.postValue("해당 선생님의 요청 승인에 실패하였습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _acceptOrDenyMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun denyStudentApplyAcademy(academyId: Long, studentId: Long){
        acceptApplyAcademyRepository.denyStudentApply(academyId, studentId).enqueue(object:
            Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue("해당 학생의 요청이 삭제되었습니다")
                }else{
                    _acceptOrDenyMessage.postValue("해당 학생의 요청을 삭제하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _acceptOrDenyMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
    fun denyTeacherApplyAcademy(academyId: Long, teacherId: Long){
        acceptApplyAcademyRepository.denyTeacherApply(academyId, teacherId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _acceptOrDenyMessage.postValue("해당 선생님의 요청이 삭제되었습니다")
                }else{
                    _acceptOrDenyMessage.postValue("해당 선생님의 요청을 삭제하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _acceptOrDenyMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}