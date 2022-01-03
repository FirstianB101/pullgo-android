package com.ich.pullgo.ui.manageClassroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.data.repository.ManageClassroomRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageClassroomViewModel @Inject constructor(
    private val repository: ManageClassroomRepository
    ): ViewModel() {
    private val _studentsAppliedClassroom = MutableLiveData<Resource<List<Student>>>()
    val studentsAppliedClassroom: LiveData<Resource<List<Student>>> = _studentsAppliedClassroom

    private val _teachersAppliedClassroom = MutableLiveData<Resource<List<Teacher>>>()
    val teachersAppliedClassroom: LiveData<Resource<List<Teacher>>> = _teachersAppliedClassroom

    private val _studentsRequestApplyClassroom = MutableLiveData<Resource<List<Student>>>()
    val studentsRequestApplyClassroom: LiveData<Resource<List<Student>>> = _studentsRequestApplyClassroom

    private val _teachersRequestApplyClassroom = MutableLiveData<Resource<List<Teacher>>>()
    val teachersRequestApplyClassroom: LiveData<Resource<List<Teacher>>> = _teachersRequestApplyClassroom

    private val _examsWithinClassroom = MutableLiveData<Resource<List<Exam>>>()
    val examsWithinClassroom: LiveData<Resource<List<Exam>>> = _examsWithinClassroom

    private val _editExam = MutableLiveData<Resource<Exam>>()
    val editExam: LiveData<Resource<Exam>> = _editExam

    private val _oneExamInfo = MutableLiveData<Resource<Exam>>()
    val oneExamInfo: LiveData<Resource<Exam>> = _oneExamInfo


    private val _manageStudentRequestMessage = MutableLiveData<Resource<String>>()
    val manageStudentRequestMessage: LiveData<Resource<String>> = _manageStudentRequestMessage

    private val _manageTeacherRequestMessage = MutableLiveData<Resource<String>>()
    val manageTeacherRequestMessage: LiveData<Resource<String>> = _manageTeacherRequestMessage

    private val _editClassroomMessage = MutableLiveData<Resource<String>>()
    val editClassroomMessage: LiveData<Resource<String>> = _editClassroomMessage

    private val _deleteClassroomMessage = MutableLiveData<Resource<String>>()
    val deleteClassroomMessage: LiveData<Resource<String>> = _deleteClassroomMessage

    private val _createClassroomMessage = MutableLiveData<Resource<String>>()
    val createClassroomMessage: LiveData<Resource<String>> = _createClassroomMessage

    private val _kickMessage = MutableLiveData<Resource<String>>()
    val kickMessage: LiveData<Resource<String>> = _kickMessage

    private val _examMessage = MutableLiveData<Resource<String>>()
    val examMessage: LiveData<Resource<String>> = _examMessage

    fun requestGetStudentsAppliedClassroom(classroomId: Long){
        _studentsAppliedClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getStudentsAppliedClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    _studentsAppliedClassroom.postValue(Resource.success(response.body()))
                }else{
                    _studentsAppliedClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetTeachersAppliedClassroom(classroomId: Long){
        _teachersAppliedClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getTeachersAppliedClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    _teachersAppliedClassroom.postValue(Resource.success(response.body()))
                }else{
                    _teachersAppliedClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetTeachersRequestApplyClassroom(classroomId: Long){
        _teachersRequestApplyClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getTeachersRequestApplyClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    _teachersRequestApplyClassroom.postValue(Resource.success(response.body()))
                }else{
                    _teachersRequestApplyClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetStudentsRequestApplyClassroom(classroomId: Long){
        _studentsRequestApplyClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getStudentsRequestApplyClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    _studentsRequestApplyClassroom.postValue(Resource.success(response.body()))
                }else{
                    _studentsRequestApplyClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetExamsWithinClassroom(classroomId: Long){
        _examsWithinClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getExamsWithinClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    _examsWithinClassroom.postValue(Resource.success(response.body()))
                }else{
                    _examsWithinClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetFinishedExams(classroomId: Long){
        _examsWithinClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getFinishedExams(classroomId).let{ response ->
                if(response.isSuccessful){
                    _examsWithinClassroom.postValue(Resource.success(response.body()))
                }else{
                    _examsWithinClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestGetCancelledExams(classroomId: Long){
        _examsWithinClassroom.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getCancelledExams(classroomId).let{ response ->
                if(response.isSuccessful){
                    _examsWithinClassroom.postValue(Resource.success(response.body()))
                }else{
                    _examsWithinClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun createClassroom(classroom: Classroom){
        _createClassroomMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.createClassroom(classroom).let { response ->
                if(response.isSuccessful){
                    _createClassroomMessage.postValue(Resource.success("반이 생성되었습니다"))
                }else{
                    _createClassroomMessage.postValue(Resource.error(response.code().toString(),"반을 생성하지 못했습니다"))
                }
            }
        }
    }

    fun kickStudentFromClassroom(classroomId: Long, studentId: Long){
        _kickMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.kickStudentFromClassroom(classroomId, studentId).let { response ->
                if(response.isSuccessful){
                    _kickMessage.postValue(Resource.success("해당 학생을 반에서 제외시켰습니다"))
                }else{
                    _kickMessage.postValue(Resource.error(response.code().toString(),"제외 과정에서 오류가 발생했습니다"))
                }
            }
        }
    }
    
    fun kickTeacherFromClassroom(classroomId: Long, teacherId: Long){
        _kickMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.kickTeacherFromClassroom(classroomId, teacherId).let { response ->
                if(response.isSuccessful){
                    _kickMessage.postValue(Resource.success("해당 선생님을 반에서 제외시켰습니다"))
                }else{
                    _kickMessage.postValue(Resource.error(response.code().toString(),"제외 과정에서 오류가 발생했습니다"))
                }
            }
        }
    }

    fun editClassroom(classroomId: Long, classroom: Classroom){
        _editClassroomMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.editClassroom(classroomId, classroom).let { response ->
                if(response.isSuccessful){
                    _editClassroomMessage.postValue(Resource.success("반 정보가 수정되었습니다"))
                }else{
                    _editClassroomMessage.postValue(Resource.error(response.code().toString(),"반 정보 수정에 실패했습니다"))
                }
            }
        }
    }

    fun deleteClassroom(classroomId: Long){
        _deleteClassroomMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.deleteClassroom(classroomId).let { response ->
                if(response.isSuccessful){
                    _deleteClassroomMessage.postValue(Resource.success("반이 삭제되었습니다"))
                }else{
                    _deleteClassroomMessage.postValue(Resource.error(response.code().toString(),"반 삭제에 실패했습니다"))
                }
            }
        }
    }

    fun acceptStudent(classroomId: Long, studentId: Long){
        _manageStudentRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.acceptStudent(classroomId,studentId).let { response ->
                if(response.isSuccessful){
                    _manageStudentRequestMessage.postValue(Resource.success("해당 학생을 반에 등록하였습니다"))
                }else{
                    _manageStudentRequestMessage.postValue(Resource.error(response.code().toString(),"학생 반 등록에 실패했습니다"))
                }
            }
        }
    }

    fun acceptTeacher(classroomId: Long, teacherId: Long){
        _manageTeacherRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.acceptTeacher(classroomId,teacherId).let { response ->
                if(response.isSuccessful){
                    _manageTeacherRequestMessage.postValue(Resource.success("해당 선생님을 반에 등록하였습니다"))
                }else{
                    _manageTeacherRequestMessage.postValue(Resource.error(response.code().toString(),"선생님 반 등록에 실패했습니다"))
                }
            }
        }
    }

    fun denyStudent(studentId: Long, classroomId: Long){
        _manageStudentRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.denyStudent(studentId,classroomId).let { response ->
                if(response.isSuccessful){
                    _manageStudentRequestMessage.postValue(Resource.success("해당 학생의 요청이 삭제되었습니다"))
                }else{
                    _manageStudentRequestMessage.postValue(Resource.error(response.code().toString(),"해당 학생의 요청을 삭제하지 못했습니다"))
                }
            }
        }
    }

    fun denyTeacher(teacherId: Long, classroomId: Long){
        _manageTeacherRequestMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.denyTeacher(teacherId, classroomId).let { response ->
                if(response.isSuccessful){
                    _manageTeacherRequestMessage.postValue(Resource.success("해당 선생님의 요청이 삭제되었습니다"))
                }else{
                    _manageTeacherRequestMessage.postValue(Resource.error(response.code().toString(),"해당 선생님의 요청을 삭제하지 못했습니다"))
                }
            }
        }
    }

    fun createExam(exam: Exam){
        _examMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.createExam(exam).let { response ->
                if(response.isSuccessful){
                    _examMessage.postValue(Resource.success("시험이 생성되었습니다"))
                }else{
                    _examMessage.postValue(Resource.error(response.code().toString(),"시험을 생성하지 못했습니다"))
                }
            }
        }
    }

    fun removeExam(examId: Long){
        _examMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.removeExam(examId).let { response ->
                if(response.isSuccessful){
                    _examMessage.postValue(Resource.success("시험이 삭제되었습니다"))
                }else{
                    _examMessage.postValue(Resource.error(response.code().toString(),"시험을 삭제하지 못했습니다"))
                }
            }
        }
    }

    fun getOneExam(examId: Long){
        _oneExamInfo.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getOneExam(examId).let{response ->
                if(response.isSuccessful){
                    _oneExamInfo.postValue(Resource.success(response.body()))
                }else{
                    _oneExamInfo.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun editExam(examId: Long, exam: Exam){
        _editExam.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.editExam(examId,exam).let{response ->
                if(response.isSuccessful){
                    _editExam.postValue(Resource.success(response.body()))
                }else{
                    _editExam.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun cancelExam(examId: Long){
        _examMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.cancelExam(examId).let { response ->
                if(response.isSuccessful){
                    _examMessage.postValue(Resource.success("시험이 취소되었습니다"))
                }else{
                    _examMessage.postValue(Resource.error(response.code().toString(),"시험을 취소하지 못했습니다"))
                }
            }
        }
    }
    
    fun finishExam(examId: Long){
        _examMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.finishExam(examId).let { response ->
                if(response.isSuccessful){
                    _examMessage.postValue(Resource.success("시험이 종료되었습니다"))
                }else{
                    _examMessage.postValue(Resource.error(response.code().toString(),"시험을 종료하지 못했습니다"))
                }
            }
        }
    }
}