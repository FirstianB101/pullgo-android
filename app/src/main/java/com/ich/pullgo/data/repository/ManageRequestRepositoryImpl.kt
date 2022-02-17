package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.data.remote.dto.toClassroom
import com.ich.pullgo.data.remote.dto.toTeacher
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.domain.repository.ManageRequestRepository
import javax.inject.Inject

class ManageRequestRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): ManageRequestRepository {
    override suspend fun getAcademiesTeacherApplying(teacherId: Long) = api.getAcademiesTeacherApplying(teacherId).map{a->a.toAcademy()}
    override suspend fun getClassroomsTeacherApplying(teacherId: Long) = api.getClassroomsTeacherApplying(teacherId).map{c->c.toClassroom()}
    override suspend fun getAcademiesStudentApplying(studentId: Long) = api.getAcademiesStudentApplying(studentId).map{a->a.toAcademy()}
    override suspend fun getClassroomsStudentApplying(studentId: Long) = api.getClassroomsStudentApplying(studentId).map{c->c.toClassroom()}

    override suspend fun removeTeacherApplyingAcademy(teacherId: Long, academyId: Long) = api.removeTeacherAppliedAcademy(teacherId, academyId)
    override suspend fun removeTeacherApplyingClassroom(teacherId: Long, classroomId: Long) = api.removeTeacherAppliedClassroom(teacherId, classroomId)
    override suspend fun removeStudentApplyingAcademy(studentId: Long, academyId: Long) = api.removeStudentAppliedAcademy(studentId, academyId)
    override suspend fun removeStudentApplyingClassroom(studentId: Long, classroomId: Long) = api.removeStudentAppliedClassroom(studentId, classroomId)

    override suspend fun getOwnerOfAcademy(ownerId: Long) = api.getOneTeacher(ownerId).toTeacher()

    override suspend fun getAcademyOfClassroom(academyId: Long) = api.getAcademyById(academyId).toAcademy()
}