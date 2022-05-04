package com.ich.pullgo.presentation.main.common.components.change_info_check_pw_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.model.User
import com.ich.pullgo.domain.use_case.change_info.ChangeInfoUseCases
import com.ich.pullgo.presentation.sign_up.util.SignUpUtils
import com.ich.pullgo.presentation.sign_up.util.SignUpUtils.isAllStudentInfoFilled
import com.ich.pullgo.presentation.sign_up.util.SignUpUtils.isAllTeacherInfoFilled
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

    init {
        _state.value = _state.value.copy(
            fullName = currentUser?.student?.account?.fullName ?: currentUser?.teacher?.account?.fullName!!,
            phone = currentUser?.student?.account?.phone ?: currentUser?.teacher?.account?.phone!!,
            parentPhone = currentUser?.student?.parentPhone,
            school = currentUser?.student?.schoolName,
            schoolYear = "${currentUser?.student?.schoolYear}학년"
        )
    }

    fun onEvent(event: ChangeInfoEvent){
        when(event){
            is ChangeInfoEvent.CheckPassword -> {
                currentUser?.let {
                    checkPassword(it,event.inputPassword)
                }
            }
            is ChangeInfoEvent.ChangeStudentInfo -> {
                if(
                    isAllStudentInfoFilled(
                        fullName = _state.value.fullName,
                        phone = _state.value.phone,
                        verify = _state.value.verify,
                        parentPhone = _state.value.parentPhone ?: "",
                        schoolName = _state.value.school ?: ""
                    )
                ) {
                    changeStudentInfo(
                        Student(
                            account = Account(
                                username = currentUser?.student?.account?.username,
                                fullName = _state.value.fullName,
                                phone = _state.value.phone,
                                password = currentUser?.student?.account?.password
                            ),
                            parentPhone = _state.value.parentPhone,
                            schoolName = _state.value.school,
                            schoolYear = when (_state.value.schoolYear) {
                                "1학년" -> 1
                                "2학년" -> 2
                                "3학년" -> 3
                                else -> throw Exception("SchoolYear Exception")
                            }
                        ).also { it.id = currentUser?.student?.id }
                    )
                }else{
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("입력하지 않은 정보가 존재합니다"))
                    }
                }
            }
            is ChangeInfoEvent.ChangeTeacherInfo -> {
                if(
                    isAllTeacherInfoFilled(
                        fullName = _state.value.fullName,
                        phone = _state.value.phone,
                        verify = _state.value.verify
                    )
                ) {
                    changeTeacherInfo(
                        Teacher(
                            Account(
                                username = currentUser?.teacher?.account?.username,
                                fullName = _state.value.fullName,
                                phone = _state.value.phone,
                                password = currentUser?.teacher?.account?.password
                            )
                        ).also { it.id = currentUser?.teacher?.id }
                    )
                }else{
                    viewModelScope.launch {
                        _eventFlow.emit(UiEvent.ShowSnackbar("입력하지 않은 정보가 존재합니다"))
                    }
                }
            }
            is ChangeInfoEvent.FullNameChanged -> {
                _state.value = _state.value.copy(
                    fullName = event.fullName
                )
            }
            is ChangeInfoEvent.PhoneChanged -> {
                _state.value = _state.value.copy(
                    phone = event.phone
                )
            }
            is ChangeInfoEvent.VerifyChanged -> {
                _state.value = _state.value.copy(
                    verify = event.verify
                )
            }
            is ChangeInfoEvent.ParentPhoneChanged -> {
                _state.value = _state.value.copy(
                    parentPhone = event.parentPhone
                )
            }
            is ChangeInfoEvent.SchoolNameChanged -> {
                _state.value = _state.value.copy(
                    school = event.school
                )
            }
            is ChangeInfoEvent.SchoolYearChanged -> {
                _state.value = _state.value.copy(
                    schoolYear = event.schoolYear
                )
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
        data class ShowSnackbar(val message: String): UiEvent()
        object GoToChangeInfoScreen: UiEvent()
        object SuccessChangingInfo: UiEvent()
    }
}