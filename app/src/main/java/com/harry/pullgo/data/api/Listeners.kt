package com.harry.pullgo.data.api

import android.view.View
import com.harry.pullgo.data.models.*

interface OnAcademyClickListener {
    fun onAcademyClick(view: View,academy: Academy?)
}

interface OnAcademyRequestListener{
    fun onAcademyClick(view: View, academy: Academy?)
    fun onRemoveRequest(view: View, academy: Academy?)
}

interface OnCheckPwListener {
    fun onPasswordChecked()
}

interface OnLessonClickListener {
    fun onLessonClick(view: View, lesson: Lesson?)
}

interface OnClassroomClickListener{
    fun onClassroomClick(view: View, classroom: Classroom?)
}

interface OnClassroomRequestListener{
    fun onClassroomClick(view: View, classroom: Classroom?)
    fun onRemoveRequest(view: View, classroom: Classroom?)
}

interface OnExamClickListener{
    fun onExamClick(view: View, exam: Exam?)
    fun onTakeExamStatusClick(view: View, exam: Exam?)
    fun onRemoveButtonClick(view: View, exam: Exam?)
    fun onManageQuestionClick(view: View, exam: Exam?)
}

interface OnStudentClickListener{
    fun onBackgroundClick(view: View, student: Student?)
    fun onApplyButtonClick(view: View, student: Student?)
    fun onRemoveButtonClick(view: View, student: Student?)
}

interface OnTeacherClickListener{
    fun onBackgroundClick(view: View, teacher: Teacher?)
    fun onApplyButtonClick(view: View, teacher: Teacher?)
    fun onRemoveButtonClick(view: View, teacher: Teacher?)
}

interface OnCalendarResetListener{
    fun onResetCalendar()
}

interface OnDataChangedListener{
    fun onChangeData(isTeacher: Boolean)
}

interface OnKickPersonListener{
    fun noticeKicked()
}

interface OnChoiceListener{
    fun onChoice(choices: List<Int>)
}

interface OnEditMultipleChoiceListener{
    fun onEditMultipleChoice(choice: Map<String,String>, answer: List<Int>)
}