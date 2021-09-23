package com.harry.pullgo.ui.lesson

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Lesson
import com.harry.pullgo.data.repository.ClassroomsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewLessonViewModel(private val classroomsRepository: ClassroomsRepository):ViewModel() {
    private val _createNewLessonClassroomRepository = MutableLiveData<List<Classroom>>()
    val createNewLessonClassroomRepository = _createNewLessonClassroomRepository

    private val _createMessage = MutableLiveData<String>()
    val createMessage = _createMessage

    fun requestGetClassrooms(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            classroomsRepository.getClassroomsByTeacherId(id).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _createNewLessonClassroomRepository.postValue(it)
                    }
                }
            }
        }
    }

    fun createNewLesson(lesson: Lesson){
        classroomsRepository.createNewLesson(lesson).enqueue(object: Callback<Lesson> {
            override fun onResponse(call: Call<Lesson>, response: Response<Lesson>) {
                if(response.isSuccessful){
                    _createMessage.postValue("수업을 생성하였습니다")
                }else{
                    _createMessage.postValue("수업을 생성하지 못했습니다")
                }
            }

            override fun onFailure(call: Call<Lesson>, t: Throwable) {
                _createMessage.postValue("서버와 연결에 실패했습니다")
            }
        })
    }
}

class CreateNewLessonViewModelFactory(private val classroomsRepository: ClassroomsRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ClassroomsRepository::class.java).newInstance(classroomsRepository)
    }
}