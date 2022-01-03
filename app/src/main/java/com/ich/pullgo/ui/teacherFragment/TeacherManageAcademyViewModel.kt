package com.ich.pullgo.ui.teacherFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.data.repository.ManageAcademyRepository
import com.ich.pullgo.data.utils.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Teacher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherManageAcademyViewModel @Inject constructor(
    private val manageAcademyRepository: ManageAcademyRepository
    ): ViewModel() {
    private val _ownedAcademiesRepository = MutableLiveData<Resource<List<Academy>>>()
    val ownedAcademiesRepository: LiveData<Resource<List<Academy>>> = _ownedAcademiesRepository

    private val _teachersAtAcademyRepository = MutableLiveData<Resource<List<Teacher>>>()
    val teachersAtAcademyRepository: LiveData<Resource<List<Teacher>>> = _teachersAtAcademyRepository

    private val _manageAcademyMessage = MutableLiveData<Resource<String>>()
    val manageAcademyMessage: LiveData<Resource<String>> = _manageAcademyMessage

    fun requestGetOwnedAcademies(ownerId: Long){
        _ownedAcademiesRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.getAcademiesByOwnerId(ownerId).let{ response ->
                if(response.isSuccessful){
                    _ownedAcademiesRepository.postValue(Resource.success(response.body()))
                }else{
                    _ownedAcademiesRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun getTeachersAtAcademy(academyId: Long){
        _ownedAcademiesRepository.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.getTeachersSuchAcademy(academyId).let{ response ->
                if(response.isSuccessful){
                    _teachersAtAcademyRepository.postValue(Resource.success(response.body()))
                }else{
                    _teachersAtAcademyRepository.postValue(Resource.error(response.code().toString(),null))
                }
            }
        }
    }

    fun editAcademy(academyId: Long, academy: Academy){
        _manageAcademyMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.editAcademy(academyId, academy).let { response ->
                if(response.isSuccessful){
                    _manageAcademyMessage.postValue(Resource.success("정보가 수정되었습니다"))
                }else{
                    _manageAcademyMessage.postValue(Resource.error(response.code().toString(),"정보를 수정하지 못했습니다"))
                }
            }
        }
    }

    fun deleteAcademy(academyId: Long){
        _manageAcademyMessage.postValue(Resource.loading(null))

        viewModelScope.launch {
            manageAcademyRepository.deleteAcademy(academyId).let { response ->
                if(response.isSuccessful){
                    _manageAcademyMessage.postValue(Resource.success("학원이 삭제되었습니다"))
                }else{
                    _manageAcademyMessage.postValue(Resource.error(response.code().toString(),"학원을 삭제하지 못했습니다"))
                }
            }
        }
    }
}