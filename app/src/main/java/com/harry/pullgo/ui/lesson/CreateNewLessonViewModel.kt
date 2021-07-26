package com.harry.pullgo.ui.lesson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.repository.ClassroomsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateNewLessonViewModel(private val classroomsRepository: ClassroomsRepository):ViewModel() {
    private val _createNewLessonClassroomRepository = MutableLiveData<List<Classroom>>()
    val createNewLessonClassroomRepository = _createNewLessonClassroomRepository

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
}

class CreateNewLessonViewModelFactory(private val classroomsRepository: ClassroomsRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ClassroomsRepository::class.java).newInstance(classroomsRepository)
    }
}