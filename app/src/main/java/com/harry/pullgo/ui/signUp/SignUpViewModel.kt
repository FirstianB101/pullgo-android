package com.harry.pullgo.ui.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harry.pullgo.data.objects.Student
import com.harry.pullgo.data.objects.Teacher

class SignUpViewModel: ViewModel() {
    private val _signUpId = MutableLiveData<String>()
    val signUpId = _signUpId

    private val _signUpPw = MutableLiveData<String>()
    val signUpPw = _signUpPw

    private val _signUpStudent = MutableLiveData<Student>()
    val signUpStudent = _signUpStudent

    private val _signUpTeacher = MutableLiveData<Teacher>()
    val signUpTeacher = _signUpTeacher
}