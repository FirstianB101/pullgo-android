package com.ich.pullgo.presentation.main.teacher_main.manage_academy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.use_case.manage_academy.ManageAcademyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageAcademyViewModel @Inject constructor(
    private val manageAcademyUseCases: ManageAcademyUseCases,
    private val app: PullgoApplication
): ViewModel() {

    private val _state = MutableStateFlow(ManageAcademyState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val teacher by lazy { app.getLoginUser()?.teacher }

    init {
        getAcademiesTeacherOwned(teacher?.id!!)
    }

    fun onEvent(event: ManageAcademyEvent){
        when(event){
            is ManageAcademyEvent.SelectAcademy -> {
                _state.value = _state.value.copy(
                    selectedAcademy = event.academy,
                    academyAddress = event.academy.address ?: "",
                    academyPhone = event.academy.phone ?: ""
                )
            }
            is ManageAcademyEvent.AcademyAddressChanged -> {
                _state.value = _state.value.copy(
                    academyAddress = event.address
                )
            }
            is ManageAcademyEvent.AcademyPhoneChanged -> {
                _state.value = _state.value.copy(
                    academyPhone = event.phone
                )
            }
            is ManageAcademyEvent.EditAcademy -> {
                val edited = Academy(
                    name = _state.value.selectedAcademy?.name,
                    phone = _state.value.academyPhone,
                    address = _state.value.academyAddress,
                    ownerId = teacher?.id
                )
                editAcademy(_state.value.selectedAcademy?.id!!, edited)
            }
            is ManageAcademyEvent.DeleteAcademy -> {
                deleteAcademy(_state.value.selectedAcademy?.id!!)
                    .invokeOnCompletion { getAcademiesTeacherOwned(teacher?.id!!) }
            }
        }
    }

    private fun getAcademiesTeacherOwned(teacherId: Long) = viewModelScope.launch {
        manageAcademyUseCases.getOwnedAcademies(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        ownedAcademies = result.data!!,
                        isLoading = false
                    )
                    if(result.data.isEmpty())
                        _eventFlow.emit(UiEvent.TransferMainActivityIfAppliedAcademyCnt1To0)
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
                    _eventFlow.emit(UiEvent.ShowToast("학원 정보를 불러오지 못했습니다. (${result.message})"))
                }
            }
        }
    }

    private fun editAcademy(academyId: Long, editedAcademy: Academy) = viewModelScope.launch {
        manageAcademyUseCases.editAcademy(academyId, editedAcademy).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원 정보가 수정되었습니다"))
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
                    _eventFlow.emit(UiEvent.ShowToast("정보를 수정하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun deleteAcademy(academyId: Long) = viewModelScope.launch {
        manageAcademyUseCases.deleteAcademy(academyId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        selectedAcademy = null,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.ShowToast("학원을 삭제했습니다"))
                    _eventFlow.emit(UiEvent.DeleteAcademySuccess)
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
                    _eventFlow.emit(UiEvent.ShowToast("학원을 삭제하지 못했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object DeleteAcademySuccess: UiEvent()
        object TransferMainActivityIfAppliedAcademyCnt1To0: UiEvent()
    }
}