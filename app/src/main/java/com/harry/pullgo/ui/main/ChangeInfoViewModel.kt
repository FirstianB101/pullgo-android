package com.harry.pullgo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ChangeInfoRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeInfoViewModel(private val changeInfoRepository: ChangeInfoRepository): ViewModel() {
    private val _changeStudent = MutableLiveData<Student>()
    val changeStudent = _changeStudent

    private val _changeTeacher = MutableLiveData<Teacher>()
    val changeTeacher = _changeTeacher

    private val _changeInfoMessage = MutableLiveData<String>()
    val changeInfoMessage: LiveData<String> = _changeInfoMessage

    fun changeStudentInfo(studentId: Long, student: Student){
        changeInfoRepository.changeStudentInfo(student.id!!,student).enqueue(object: Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if(response.isSuccessful){
                    _changeInfoMessage.postValue("계정 정보가 수정되었습니다")
                }else{
                    _changeInfoMessage.postValue("계정 정보 수정에 실패했습니다")
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                _changeInfoMessage.postValue("서버와 연결에 실패하였습니다")
            }
        })
    }

    fun changeTeacherInfo(teacherId: Long, teacher: Teacher){
        changeInfoRepository.changeTeacherInfo(teacher.id!!,teacher).enqueue(object: Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                if (response.isSuccessful) {
                    _changeInfoMessage.postValue("계정 정보가 수정되었습니다")
                } else {
                    _changeInfoMessage.postValue("계정 정보 수정에 실패했습니다")
                }
            }

            override fun onFailure(call: Call<Teacher>, t: Throwable) {
                _changeInfoMessage.postValue("서버와 연결에 실패하였습니다")
            }
        })
    }
}

class ChangeInfoViewModelFactory(private val changeInfoRepository: ChangeInfoRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ChangeInfoRepository::class.java).newInstance(changeInfoRepository)
    }
}