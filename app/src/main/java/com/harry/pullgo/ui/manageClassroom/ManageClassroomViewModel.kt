package com.harry.pullgo.ui.manageClassroom


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Student
import com.harry.pullgo.data.models.Teacher
import com.harry.pullgo.data.repository.ManageClassroomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageClassroomViewModel(private val repository: ManageClassroomRepository): ViewModel() {

    private val _studentsAppliedClassroom = MutableLiveData<List<Student>>()
    val studentsAppliedClassroom = _studentsAppliedClassroom

    private val _teachersAppliedClassroom = MutableLiveData<List<Teacher>>()
    val teachersAppliedClassroom = _teachersAppliedClassroom

    private val _studentsRequestApplyClassroom = MutableLiveData<List<Student>>()
    val studentsRequestApplyClassroom = _studentsRequestApplyClassroom

    private val _teachersRequestApplyClassroom = MutableLiveData<List<Teacher>>()
    val teachersRequestApplyClassroom = _teachersRequestApplyClassroom

    private val _createClassroomMessage = MutableLiveData<String>()
    val createClassroomMessage = _createClassroomMessage

    private val _kickMessage = MutableLiveData<String>()
    val kickMessage = _kickMessage

    private val _editClassroomMessage = MutableLiveData<String>()
    val editClassroomMessage = _editClassroomMessage

    private val _manageRequestMessage = MutableLiveData<String>()
    val manageRequestMessage = _manageRequestMessage

    fun requestGetStudentsAppliedClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getStudentsAppliedClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsAppliedClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetTeachersAppliedClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getTeachersAppliedClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersAppliedClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetTeachersRequestApplyClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getTeachersRequestApplyClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _teachersRequestApplyClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun requestGetStudentsRequestApplyClassroom(classroomId: Long){
        CoroutineScope(Dispatchers.IO).launch {
            repository.getStudentsRequestApplyClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _studentsRequestApplyClassroom.postValue(it)
                    }
                }
            }
        }
    }

    fun createClassroom(classroom: Classroom){
        repository.createClassroom(classroom).enqueue(object: Callback<Classroom> {
            override fun onResponse(call: Call<Classroom>, response: Response<Classroom>) {
                if(response.isSuccessful){
                    _createClassroomMessage.postValue("반이 생성되었습니다")
                }else{
                    _createClassroomMessage.postValue("반을 생성하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Classroom>, t: Throwable) {
                _createClassroomMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun kickStudentFromClassroom(classroomId: Long, studentId: Long){
        repository.kickStudentFromClassroom(classroomId, studentId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _kickMessage.postValue("해당 학생을 반에서 제외시켰습니다")
                }else{
                    _kickMessage.postValue("해당 반에 존재하지 않는 학생입니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _kickMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun editClassroom(classroomId: Long, classroom: Classroom){
        repository.editClassroom(classroomId, classroom).enqueue(object: Callback<Classroom> {
            override fun onResponse(call: Call<Classroom>, response: Response<Classroom>) {
                if(response.isSuccessful){
                    _editClassroomMessage.postValue("수정이 완료되었습니다")
                }else{
                    _editClassroomMessage.postValue("수정하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Classroom>, t: Throwable) {
                _editClassroomMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun deleteClassroom(classroomId: Long){
        repository.deleteClassroom(classroomId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _editClassroomMessage.postValue("반이 삭제되었습니다")
                }else{
                    _editClassroomMessage.postValue("반을 삭제하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _editClassroomMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun acceptStudent(classroomId: Long, studentId: Long){
        repository.acceptStudent(classroomId,studentId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _manageRequestMessage.postValue("학생 반 등록이 승인되었습니다")
                }else{
                    _manageRequestMessage.postValue("학생 반 등록에 실패했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _manageRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun acceptTeacher(classroomId: Long, teacherId: Long){
        repository.acceptStudent(classroomId,teacherId).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _manageRequestMessage.postValue("선생님 반 등록이 승인되었습니다")
                }else{
                    _manageRequestMessage.postValue("선생님 반 등록에 실패했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _manageRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun denyStudent(studentId: Long, classroomId: Long){
        repository.denyStudent(studentId,classroomId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _manageRequestMessage.postValue("해당 학생의 요청이 삭제되었습니다")
                }else{
                    _manageRequestMessage.postValue("해당 학생의 요청을 삭제하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _manageRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun denyTeacher(teacherId: Long, classroomId: Long){
       repository.denyTeacher(teacherId, classroomId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _manageRequestMessage.postValue("해당 선생님의 요청이 삭제되었습니다")
                }else{
                    _manageRequestMessage.postValue("해당 선생님의 요청을 삭제하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _manageRequestMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}

class ManageClassroomViewModelFactory(private val repository: ManageClassroomRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ManageClassroomRepository::class.java).newInstance(repository)
    }
}