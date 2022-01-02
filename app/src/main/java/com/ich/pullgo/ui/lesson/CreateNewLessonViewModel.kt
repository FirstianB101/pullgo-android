package com.ich.pullgo.ui.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.data.models.Classroom
import com.ich.pullgo.data.models.Lesson
import com.ich.pullgo.data.repository.ClassroomsRepository
import com.ich.pullgo.data.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewLessonViewModel @Inject constructor(
    private val classroomsRepository: ClassroomsRepository
    ):ViewModel() {
    private val _createNewLessonClassroomRepository = MutableLiveData<Resource<List<Classroom>>>()
    val createNewLessonClassroomRepository: LiveData<Resource<List<Classroom>>> = _createNewLessonClassroomRepository

    private val _createMessage = MutableLiveData<Resource<String>>()
    val createMessage: LiveData<Resource<String>> = _createMessage

    fun requestGetClassrooms(id: Long){
        _createNewLessonClassroomRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            classroomsRepository.getClassroomsByTeacherId(id).let{ response ->
                if(response.isSuccessful){
                    _createNewLessonClassroomRepository.postValue(Resource.success(response.body()))
                }else{
                    _createNewLessonClassroomRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun createNewLesson(lesson: Lesson){
        _createMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            classroomsRepository.createNewLesson(lesson).let { response ->
                if(response.isSuccessful){
                    _createMessage.postValue(Resource.success("수업을 생성하였습니다"))
                }else{
                    _createMessage.postValue(Resource.error(response.code().toString(),"수업을 생성하지 못했습니다"))
                }
            }
        }
    }
}