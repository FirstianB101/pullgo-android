package com.harry.pullgo.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Academy
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LessonsViewModel(private val lessonsRepository: LessonsRepository): ViewModel() {
    private val _allLessonRepositories = MutableLiveData<List<Lesson>>()
    val allLessonsRepositories: LiveData<List<Lesson>> = _allLessonRepositories

    private val _dayLessonsRepositories = MutableLiveData<List<Lesson>>()
    val dayLessonsRepositories: LiveData<List<Lesson>> = _dayLessonsRepositories

    private val _classroomInfoRepository = MutableLiveData<Classroom>()
    val classroomInfoRepository: LiveData<Classroom> = _classroomInfoRepository

    private val _academyInfoRepository = MutableLiveData<Academy>()
    val academyInfoRepository: LiveData<Academy> = _academyInfoRepository

    private val _lessonMessage = MutableLiveData<String>()
    val lessonMessage: LiveData<String> = _lessonMessage

    fun requestStudentLessons(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            lessonsRepository.getStudentLessons(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _allLessonRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherLessons(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            lessonsRepository.getTeacherLessons(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _allLessonRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun requestStudentLessonOnDate(id: Long, date: String){
        CoroutineScope(Dispatchers.IO).launch {
            lessonsRepository.getStudentLessonOnDate(id,date).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _dayLessonsRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun requestTeacherLessonOnDate(id: Long, date: String){
        CoroutineScope(Dispatchers.IO).launch {
            lessonsRepository.getTeacherLessonOnDate(id,date).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _dayLessonsRepositories.postValue(it)
                    }
                }
            }
        }
    }

    fun getClassroomInfoOfLesson(lesson: Lesson){
        CoroutineScope(Dispatchers.IO).launch {
            lessonsRepository.getClassroomSuchLesson(lesson.classroomId!!).let { response ->
                if(response.isSuccessful){
                    response.body().let{
                        _classroomInfoRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun getAcademyInfoOfLesson(lesson: Lesson){
        CoroutineScope(Dispatchers.IO).launch {
            val classroom = lessonsRepository.getClassroomSuchLesson(lesson.classroomId!!).body()

            lessonsRepository.getAcademySuchClassroom(classroom?.academyId!!).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _academyInfoRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun patchLessonInfo(lessonId: Long, lesson: Lesson){
        lessonsRepository.requestPatchLessonInfo(lessonId,lesson).enqueue(object: Callback<Lesson> {
            override fun onResponse(call: Call<Lesson>, response: Response<Lesson>) {
                if(response.isSuccessful){
                    _lessonMessage.postValue("수업 정보가 변경되었습니다")
                }else{
                    _lessonMessage.postValue("수업 정보가 변경에 실패했습니다")
                }
            }

            override fun onFailure(call: Call<Lesson>, t: Throwable) {
                _lessonMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }

    fun deleteLesson(lessonId: Long){
        lessonsRepository.requestDeleteLesson(lessonId).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    _lessonMessage.postValue("수업이 삭제되었습니다")
                }else{
                    _lessonMessage.postValue("수업 삭제에 실패했습니다")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                _lessonMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}

class LessonsViewModelFactory(private val lessonsRepository: LessonsRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LessonsRepository::class.java).newInstance(lessonsRepository)
    }
}