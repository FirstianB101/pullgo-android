package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.*
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.*
import com.ich.pullgo.domain.repository.ManageClassroomRepository
import javax.inject.Inject

class ManageClassroomRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ManageClassroomRepository {
    override suspend fun getClassroomsTeacherApplied(teacherId: Long): List<Classroom> = api.getClassroomsByTeacherId(teacherId).map{c->c.toClassroom()}
    override suspend fun getAcademiesTeacherApplied(teacherId: Long): List<Academy> = api.getAcademiesTeacherApplied(teacherId).map{a->a.toAcademy()}
    override suspend fun getStudentsAppliedClassroom(classroomId: Long) = api.getStudentsAppliedClassroom(classroomId).map{s->s.toStudent()}
    override suspend fun getTeachersAppliedClassroom(classroomId: Long) = api.getTeachersAppliedClassroom(classroomId).map{t->t.toTeacher()}
    override suspend fun getStudentsRequestApplyClassroom(classroomId: Long) = api.getStudentsRequestApplyClassroom(classroomId).map{s->s.toStudent()}
    override suspend fun getTeachersRequestApplyClassroom(classroomId: Long) = api.getTeachersRequestApplyClassroom(classroomId).map{t->t.toTeacher()}
    override suspend fun getExamsWithinClassroom(classroomId: Long) = api.getClassroomExams(classroomId,100).map{e->e.toExam()}
    override suspend fun getFinishedExams(classroomId: Long) = api.getClassroomFinishedExams(classroomId,"true",100).map{e->e.toExam()}
    override suspend fun getCancelledExams(classroomId: Long) = api.getClassroomCancelledExams(classroomId,"true",100).map{e->e.toExam()}

    override suspend fun createClassroom(classroom: Classroom) = api.createClassroom(classroom).toClassroom()
    override suspend fun kickStudentFromClassroom(classroomId: Long, studentId: Long) = api.kickStudentFromClassroom(classroomId, studentId)
    override suspend fun kickTeacherFromClassroom(classroomId: Long, teacherId: Long) = api.kickTeacherFromClassroom(classroomId, teacherId)
    override suspend fun editClassroom(classroomId: Long, classroom: Classroom) = api.editClassroom(classroomId, classroom).toClassroom()
    override suspend fun deleteClassroom(classroomId: Long) = api.deleteClassroom(classroomId)
    override suspend fun createExam(exam: Exam) = api.createExam(exam).toExam()
    override suspend fun deleteExam(examId: Long) = api.deleteExam(examId)
    override suspend fun editExam(examId: Long, exam: Exam) = api.editExam(examId, exam).toExam()
    override suspend fun cancelExam(examId: Long) = api.cancelExam(examId)
    override suspend fun finishExam(examId: Long) = api.finishExam(examId)

    override suspend fun acceptStudent(classroomId: Long, studentId: Long) = api.acceptStudentApplyClassroom(classroomId, studentId)
    override suspend fun acceptTeacher(classroomId: Long, teacherId: Long) = api.acceptTeacherApplyClassroom(classroomId, teacherId)
    override suspend fun denyStudent(studentId: Long, classroomId: Long) = api.removeStudentClassroomRequest(studentId, classroomId)
    override suspend fun denyTeacher(teacherId: Long, classroomId: Long) = api.removeTeacherClassroomRequest(teacherId, classroomId)

    override suspend fun getOneExam(examId: Long) = api.getOneExam(examId).toExam()
    override suspend fun getAttenderStatesInExam(examId: Long): List<AttenderState> = api.getExamAttenderStates(examId,100).map{s->s.toAttenderState()}
    override suspend fun getOneStudent(studentId: Long): Student = api.getOneStudent(studentId).toStudent()
}