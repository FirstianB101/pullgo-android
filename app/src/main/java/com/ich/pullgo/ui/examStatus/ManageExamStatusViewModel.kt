package com.ich.pullgo.ui.examStatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
<<<<<<< HEAD:app/src/main/java/com/ich/pullgo/ui/examStatus/ManageExamStatusViewModel.kt
import com.ich.pullgo.data.models.AttenderState
import com.ich.pullgo.data.models.Student
import com.ich.pullgo.data.repository.ExamStatusRepository
import com.ich.pullgo.data.utils.Resource
=======
import com.ich.pullgo.data.repository.ExamStatusRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Student
>>>>>>> ich:app/src/main/java/com/harry/pullgo/ui/examStatus/ManageExamStatusViewModel.kt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageExamStatusViewModel @Inject constructor(
    private val repository: ExamStatusRepository
): ViewModel() {
    private val _attenderStatesInExam = MutableLiveData<Resource<List<AttenderState>>>()
    val attenderStatesInExam: LiveData<Resource<List<AttenderState>>> = _attenderStatesInExam

    private val _oneStudent = MutableLiveData<Resource<Student>>()
    val oneStudent: LiveData<Resource<Student>> = _oneStudent

    private val _studentsInClassroom = MutableLiveData<Resource<List<Student>>>()
    val studentsInClassroom: LiveData<Resource<List<Student>>> = _studentsInClassroom

    fun requestGetAttenderStatesInExam(examId: Long){
        _attenderStatesInExam.postValue(Resource.loading(null))

        viewModelScope.launch {
            repository.getAttenderStatesInExam(examId).let { response ->
                if(response.isSuccessful){
                    _attenderStatesInExam.postValue(Resource.success(response.body()))
                }else{
                    _attenderStatesInExam.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getOneStudent(studentId: Long){
        viewModelScope.launch {
            repository.getOneStudent(studentId).let{response ->
                if(response.isSuccessful){
                    _oneStudent.postValue(Resource.success(response.body()))
                }else{
                    _oneStudent.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getStudentsInClassroom(classroomId: Long){
        viewModelScope.launch {
            repository.getStudentsInClassroom(classroomId).let{ response ->
                if(response.isSuccessful){
                    _studentsInClassroom.postValue(Resource.success(response.body()))
                }else{
                    _studentsInClassroom.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }
}