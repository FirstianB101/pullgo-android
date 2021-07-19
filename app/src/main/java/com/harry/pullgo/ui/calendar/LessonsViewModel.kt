package com.harry.pullgo.ui.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.repository.LessonsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LessonsViewModel(private val lessonsRepository: LessonsRepository):ViewModel() {
    private val _allLessonRepositories = MutableLiveData<List<Lesson>>()
    val allLessonsRepositories = _allLessonRepositories

    private val _dayLessonsRepositories = MutableLiveData<List<Lesson>>()
    val dayLessonsRepositories = _dayLessonsRepositories

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
}