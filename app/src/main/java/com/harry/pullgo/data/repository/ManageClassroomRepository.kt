package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Classroom
import com.harry.pullgo.data.models.Exam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageClassroomRepository @Inject constructor(
    private val manageClassroomClient: PullgoService
) {
    suspend fun getStudentsAppliedClassroom(classroomId: Long) = manageClassroomClient.getStudentsAppliedClassroom(classroomId)
    suspend fun getTeachersAppliedClassroom(classroomId: Long) = manageClassroomClient.getTeachersAppliedClassroom(classroomId)
    suspend fun getStudentsRequestApplyClassroom(classroomId: Long) = manageClassroomClient.getStudentsRequestApplyClassroom(classroomId)
    suspend fun getTeachersRequestApplyClassroom(classroomId: Long) = manageClassroomClient.getTeachersRequestApplyClassroom(classroomId)
    suspend fun getExamsWithinClassroom(classroomId: Long) = manageClassroomClient.getClassroomExams(classroomId)

    fun createClassroom(classroom: Classroom) = manageClassroomClient.createClassroom(classroom)
    fun kickStudentFromClassroom(classroomId: Long, studentId: Long) = manageClassroomClient.kickStudentFromClassroom(classroomId, studentId)
    fun kickTeacherFromClassroom(classroomId: Long, teacherId: Long) = manageClassroomClient.kickTeacherFromClassroom(classroomId, teacherId)
    fun editClassroom(classroomId: Long, classroom: Classroom) = manageClassroomClient.editClassroom(classroomId, classroom)
    fun deleteClassroom(classroomId: Long) = manageClassroomClient.deleteClassroom(classroomId)
    fun createExam(exam: Exam) = manageClassroomClient.createExam(exam)
    fun removeExam(examId: Long) = manageClassroomClient.removeExam(examId)
    fun cancelExam(examId: Long) = manageClassroomClient.cancelExam(examId)
    fun finishExam(examId: Long) = manageClassroomClient.finishExam(examId)

    fun acceptStudent(classroomId: Long, studentId: Long) = manageClassroomClient.acceptStudentApplyClassroom(classroomId, studentId)
    fun acceptTeacher(classroomId: Long, teacherId: Long) = manageClassroomClient.acceptTeacherApplyClassroom(classroomId, teacherId)
    fun denyStudent(studentId: Long, classroomId: Long) = manageClassroomClient.removeStudentClassroomRequest(studentId, classroomId)
    fun denyTeacher(teacherId: Long, classroomId: Long) = manageClassroomClient.removeTeacherClassroomRequest(teacherId, classroomId)
}