package com.harry.pullgo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher

class ChangeInfoViewModel: ViewModel() {
    private val _changeStudent = MutableLiveData<Student>()
    val changeStudent = _changeStudent

    private val _changeTeacher = MutableLiveData<Teacher>()
    val changeTeacher = _changeTeacher
}