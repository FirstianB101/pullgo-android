package com.ich.pullgo.presentation.main.common.components.apply_academy_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.domain.use_case.apply_academy.ApplyAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyAcademyViewModel @Inject constructor(
    private val applyAcademyUseCases: ApplyAcademyUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(ApplyAcademyState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val user by lazy { app.getLoginUser() }

    fun onEvent(event: ApplyAcademyEvent){
        when(event){
            is ApplyAcademyEvent.SearchQueryChanged -> {
                _state.value = _state.value.copy(
                    searchQuery = event.query
                )
            }
            is ApplyAcademyEvent.SelectAcademy -> {
                _state.value = _state.value.copy(
                    selectedAcademy = event.academy
                )
            }
            is ApplyAcademyEvent.NewAcademyNameChanged -> {
                _state.value = _state.value.copy(
                    newAcademyName = event.name
                )
            }
            is ApplyAcademyEvent.NewAcademyAddressChanged -> {
                _state.value = _state.value.copy(
                    newAcademyAddress = event.address
                )
            }
            is ApplyAcademyEvent.NewAcademyPhoneChanged -> {
                _state.value = _state.value.copy(
                    newAcademyPhone = event.phone
                )
            }
            is ApplyAcademyEvent.SearchAcademies -> {
                searchAcademy(_state.value.searchQuery)
            }
            is ApplyAcademyEvent.CreateAcademy -> {
                createAcademy(
                    Academy(
                        name = _state.value.newAcademyName,
                        address = _state.value.newAcademyAddress,
                        phone = _state.value.newAcademyPhone,
                        ownerId = user?.teacher?.id!!
                    )
                )
                resetNewAcademyInfo()
            }
            is ApplyAcademyEvent.RequestApplyingAcademy -> {
                user?.doJob(
                    ifStudent = { requestStudentApplyAcademy(user?.student?.id!!, _state.value.selectedAcademy?.id!!) },
                    ifTeacher = { requestTeacherApplyAcademy(user?.teacher?.id!!, _state.value.selectedAcademy?.id!!) }
                )
            }
        }
    }

    private fun searchAcademy(query: String) = viewModelScope.launch {
        applyAcademyUseCases.getSearchedAcademies(query).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        searchedAcademies = result.data!!,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("검색 결과를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun createAcademy(newAcademy: Academy) = viewModelScope.launch {
        applyAcademyUseCases.createAcademy(newAcademy).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원을 생성했습니다."))
                    _eventFlow.emit(UiEvent.TransferMainActivityIfAppliedAcademyCnt0To1)
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("검색 결과를 불러오지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun requestStudentApplyAcademy(studentId: Long, academyId: Long) = viewModelScope.launch {
        applyAcademyUseCases.studentApplyAcademy(studentId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원에 가입을 요청하였습니다."))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청에 실패했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun requestTeacherApplyAcademy(teacherId: Long, academyId: Long) = viewModelScope.launch {
        applyAcademyUseCases.teacherApplyAcademy(teacherId, academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원에 가입을 요청하였습니다."))
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("가입 요청에 실패했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun resetNewAcademyInfo(){
        _state.value = _state.value.copy(
            newAcademyName = "",
            newAcademyAddress = "",
            newAcademyPhone = ""
        )
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        // 가입된 학원 개수가 0개에서 1개로 되었을 경우 학원 존재하는 메인액티비티로
        object TransferMainActivityIfAppliedAcademyCnt0To1: UiEvent()
    }
}