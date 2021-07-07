package com.harry.pullgo.ui.findAcademy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harry.pullgo.data.objects.Academy
import com.harry.pullgo.data.repository.FindAcademyRepository
import com.harry.pullgo.data.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FindAcademyViewModel(private val findAcademyRepository: FindAcademyRepository): ViewModel() {
    private val _findAcademyRepositories = MutableLiveData<List<Academy>>()
    val findAcademyRepositories = _findAcademyRepositories

    fun requestGetAcademies(name: String){
        CoroutineScope(Dispatchers.IO).launch{
            findAcademyRepository.getAcademies(name).let{ response ->
                if(response.isSuccessful){
                    response.body().let{
                        _findAcademyRepositories.postValue(it)
                    }
                }
            }
        }
    }
}

class FindAcademyViewModelFactory(private val findAcademyRepository: FindAcademyRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FindAcademyRepository::class.java).newInstance(findAcademyRepository)
    }
}