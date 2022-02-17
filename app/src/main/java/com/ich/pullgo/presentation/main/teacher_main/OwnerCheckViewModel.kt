package com.ich.pullgo.presentation.main.teacher_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ich.pullgo.common.util.Resource
import com.ich.pullgo.domain.use_case.academy_owner.AcademyOwnerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerCheckViewModel @Inject constructor(
    private val academyOwnerUseCase: AcademyOwnerUseCase
): ViewModel(){

    private val _state = MutableStateFlow<Boolean>(false)
    val state: StateFlow<Boolean> = _state

    fun isOwner(teacherId: Long) = viewModelScope.launch {
        academyOwnerUseCase(teacherId).collect { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = result.data!!
                }
                is Resource.Error -> {
                    _state.value = false
                }
                is Resource.Loading -> {
                }
            }
        }
    }
}