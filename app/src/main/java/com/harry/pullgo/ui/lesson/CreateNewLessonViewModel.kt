package com.harry.pullgo.ui.lesson

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.objects.Classroom
import com.harry.pullgo.data.objects.Lesson
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.repository.CreateNewLessonRepository
import com.harry.pullgo.data.repository.FindAcademyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewLessonViewModel(private val createNewLessonRepository: CreateNewLessonRepository):ViewModel() {
    private val _createNewLessonRepositories = MutableLiveData<List<Classroom>>()
    val createNewLessonRepositories = _createNewLessonRepositories

    fun requestGetClassrooms(id: Long){
        CoroutineScope(Dispatchers.IO).launch {
            createNewLessonRepository.getClassroomsByTeacherId(id).let{response ->
                if(response.isSuccessful){
                    response.body().let{
                        _createNewLessonRepositories.postValue(it)
                    }
                }
            }
        }
    }
}

class CreateNewLessonViewModelFactory(private val createNewLessonRepository: CreateNewLessonRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CreateNewLessonRepository::class.java).newInstance(createNewLessonRepository)
    }
}