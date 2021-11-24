package com.harry.pullgo.ui.calendar

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import com.harry.pullgo.data.utils.Resource
import kotlinx.coroutines.launch

class LessonsViewModel @ViewModelInject constructor(
    private val lessonsRepository: LessonsRepository
    ): ViewModel() {
    private val _allLessonRepositories = MutableLiveData<Resource<List<Lesson>>>()
    val allLessonsRepositories: LiveData<Resource<List<Lesson>>> = _allLessonRepositories

    private val _dayLessonsRepositories = MutableLiveData<Resource<List<Lesson>>>()
    val dayLessonsRepositories: LiveData<Resource<List<Lesson>>> = _dayLessonsRepositories

    private val _classroomInfoRepository = MutableLiveData<Resource<Classroom>>()
    val classroomInfoRepository: LiveData<Resource<Classroom>> = _classroomInfoRepository

    private val _academyInfoRepository = MutableLiveData<Resource<Academy>>()
    val academyInfoRepository: LiveData<Resource<Academy>> = _academyInfoRepository

    private val _lessonMessage = MutableLiveData<Resource<String>>()
    val lessonMessage: LiveData<Resource<String>> = _lessonMessage

    fun requestStudentLessons(id: Long){
        _allLessonRepositories.postValue(Resource.loading(null))

        viewModelScope.launch {
            lessonsRepository.getStudentLessonsOnMonth(id).let{ response ->
                if(response.isSuccessful){
                    _allLessonRepositories.postValue(Resource.success(response.body()))
                }else{
                    _allLessonRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestTeacherLessons(id: Long){
        _allLessonRepositories.postValue(Resource.loading(null))

        viewModelScope.launch {
            lessonsRepository.getTeacherLessonsOnMonth(id).let{ response ->
                if(response.isSuccessful){
                    _allLessonRepositories.postValue(Resource.success(response.body()))
                }else{
                    _allLessonRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestStudentLessonOnDate(id: Long, date: String){
        _dayLessonsRepositories.postValue(Resource.loading(null))

        viewModelScope.launch {
            lessonsRepository.getStudentLessonsOnDate(id,date).let{ response ->
                if(response.isSuccessful){
                    _dayLessonsRepositories.postValue(Resource.success(response.body()))
                }else{
                    _dayLessonsRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun requestTeacherLessonOnDate(id: Long, date: String){
        _dayLessonsRepositories.postValue(Resource.loading(null))

        viewModelScope.launch {
            lessonsRepository.getTeacherLessonsOnDate(id,date).let{ response ->
                if(response.isSuccessful){
                    _dayLessonsRepositories.postValue(Resource.success(response.body()))
                }else{
                    _dayLessonsRepositories.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getClassroomInfoOfLesson(lesson: Lesson){
        viewModelScope.launch {
            lessonsRepository.getClassroomSuchLesson(lesson.classroomId!!).let { response ->
                if(response.isSuccessful){
                    _classroomInfoRepository.postValue(Resource.success(response.body()))
                }else{
                    _classroomInfoRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getAcademyInfoOfLesson(lesson: Lesson){
        viewModelScope.launch {
            lessonsRepository.getAcademySuchClassroom(lesson.academyId!!).let{ response ->
                if(response.isSuccessful){
                    _academyInfoRepository.postValue(Resource.success(response.body()))
                }else{
                    _academyInfoRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun patchLessonInfo(lessonId: Long, lesson: Lesson){
        _lessonMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            lessonsRepository.requestPatchLessonInfo(lessonId, lesson).let { response ->
                if(response.isSuccessful){
                    _lessonMessage.postValue(Resource.success("수업 정보가 변경되었습니다"))
                }else{
                    _lessonMessage.postValue(Resource.error(response.code().toString(),"수업 정보 변경에 실패했습니다"))
                }
            }
        }
    }

    fun deleteLesson(lessonId: Long){
        _lessonMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            lessonsRepository.requestDeleteLesson(lessonId).let { response ->
                if(response.isSuccessful){
                    _lessonMessage.postValue(Resource.success("수업 정보가 변경되었습니다"))
                }else{
                    _lessonMessage.postValue(Resource.error(response.code().toString(),"수업 정보 변경에 실패했습니다"))
                }
            }
        }
    }
}