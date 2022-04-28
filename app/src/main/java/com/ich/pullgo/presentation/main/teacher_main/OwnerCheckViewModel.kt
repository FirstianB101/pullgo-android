package com.ich.pullgo.presentation.main.teacher_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.academy_owner.AcademyOwnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerCheckViewModel @Inject constructor(
    private val academyOwnerUseCase: AcademyOwnerUseCase,
    private val app: PullgoApplication
): ViewModel(){

    private val _state = MutableStateFlow(false)
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val teacher by lazy { app.getLoginUser()?.teacher }

    init{
        isOwner(teacher?.id!!)
    }

    fun isOwner(teacherId: Long) = viewModelScope.launch {
        academyOwnerUseCase(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = result.data!!
                }
                is Resource.Error -> {
                    _state.value = false
                    _eventFlow.emit(UiEvent.ShowToast("학원장 여부 정보를 가져오지 못했습니다 (${result.message})"))
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowToast(val message: String): UiEvent()
    }
}