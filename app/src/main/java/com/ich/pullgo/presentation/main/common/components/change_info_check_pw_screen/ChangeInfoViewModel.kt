package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.domain.use_case.change_info.ChangeInfoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeInfoViewModel @Inject constructor(
    private val changeInfoUseCases: ChangeInfoUseCases,
    private val app: PullgoApplication
): ViewModel(){

    private val _state = MutableStateFlow(ChangeInfoState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val currentUser by lazy { app.getLoginUser() }

    fun onEvent(event: ChangeInfoEvent){
        when(event){
            is ChangeInfoEvent.CheckPassword -> {
                currentUser?.let {
                    checkPassword(it,event.inputPassword)
                }
            }
            is ChangeInfoEvent.ChangeStudentInfo -> {
                changeStudentInfo(event.student)
            }
            is ChangeInfoEvent.ChangeTeacherInfo -> {
                changeTeacherInfo(event.teacher)
            }
        }
    }

    private fun checkPassword(user: User, password: String) = viewModelScope.launch {
        changeInfoUseCases.checkUserPassword(user,password).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.GoToChangeInfoScreen)
                    currentUser?.token = result.data?.token
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
                    _eventFlow.emit(UiEvent.ShowToast("비밀번호 확인에 실패했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun changeStudentInfo(student: Student) = viewModelScope.launch {
        changeInfoUseCases.changeStudentInfo(student).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        patchedStudent = result.data!!,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.SuccessChangingInfo)
                    _eventFlow.emit(UiEvent.ShowToast("유저 정보를 변경하였습니다."))
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
                    _eventFlow.emit(UiEvent.ShowToast("정보 변경에 실패했습니다 (${result.message})"))
                }
            }
        }
    }

    private fun changeTeacherInfo(teacher: Teacher) = viewModelScope.launch {
        changeInfoUseCases.changeTeacherInfo(teacher).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        patchedTeacher = result.data!!,
                        isLoading = false
                    )
                    _eventFlow.emit(UiEvent.SuccessChangingInfo)
                    _eventFlow.emit(UiEvent.ShowToast("유저 정보를 변경하였습니다."))
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
                    _eventFlow.emit(UiEvent.ShowToast("정보 변경에 실패했습니다 (${result.message})"))
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
        object GoToChangeInfoScreen: UiEvent()
        object SuccessChangingInfo: UiEvent()
    }
}