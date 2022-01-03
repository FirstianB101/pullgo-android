package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Exam
import javax.inject.Inject

class ManageClassroomRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
) {
    suspend fun getStudentsAppliedClassroom(classroomId: Long) = api.getStudentsAppliedClassroom(classroomId)
    suspend fun getTeachersAppliedClassroom(classroomId: Long) = api.getTeachersAppliedClassroom(classroomId)
    suspend fun getStudentsRequestApplyClassroom(classroomId: Long) = api.getStudentsRequestApplyClassroom(classroomId)
    suspend fun getTeachersRequestApplyClassroom(classroomId: Long) = api.getTeachersRequestApplyClassroom(classroomId)
    suspend fun getExamsWithinClassroom(classroomId: Long) = api.getClassroomExams(classroomId,100)
    suspend fun getFinishedExams(classroomId: Long) = api.getClassroomFinishedExams(classroomId,"true",100)
    suspend fun getCancelledExams(classroomId: Long) = api.getClassroomCancelledExams(classroomId,"true",100)

    suspend fun createClassroom(classroom: Classroom) = api.createClassroom(classroom)
    suspend fun kickStudentFromClassroom(classroomId: Long, studentId: Long) = api.kickStudentFromClassroom(classroomId, studentId)
    suspend fun kickTeacherFromClassroom(classroomId: Long, teacherId: Long) = api.kickTeacherFromClassroom(classroomId, teacherId)
    suspend fun editClassroom(classroomId: Long, classroom: Classroom) = api.editClassroom(classroomId, classroom)
    suspend fun deleteClassroom(classroomId: Long) = api.deleteClassroom(classroomId)
    suspend fun createExam(exam: Exam) = api.createExam(exam)
    suspend fun removeExam(examId: Long) = api.removeExam(examId)
    suspend fun editExam(examId: Long, exam: Exam) = api.editExam(examId, exam)
    suspend fun cancelExam(examId: Long) = api.cancelExam(examId)
    suspend fun finishExam(examId: Long) = api.finishExam(examId)
    suspend fun getOneExam(examId: Long) = api.getOneExam(examId)

    suspend fun acceptStudent(classroomId: Long, studentId: Long) = api.acceptStudentApplyClassroom(classroomId, studentId)
    suspend fun acceptTeacher(classroomId: Long, teacherId: Long) = api.acceptTeacherApplyClassroom(classroomId, teacherId)
    suspend fun denyStudent(studentId: Long, classroomId: Long) = api.removeStudentClassroomRequest(studentId, classroomId)
    suspend fun denyTeacher(teacherId: Long, classroomId: Long) = api.removeTeacherClassroomRequest(teacherId, classroomId)
}