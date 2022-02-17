package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson
import retrofit2.Response

interface LessonsRepository {

    suspend fun getStudentLessonsOnDate(id: Long, date: String): List<Lesson>
    suspend fun getTeacherLessonsOnDate(id: Long, date: String): List<Lesson>
    suspend fun getStudentLessonsOnMonth(id: Long): List<Lesson>
    suspend fun getTeacherLessonsOnMonth(id: Long): List<Lesson>

    suspend fun getClassroomsTeacherApplied(teacherId: Long): List<Classroom>

    suspend fun getClassroomSuchLesson(classroomId: Long): Classroom
    suspend fun getAcademySuchLesson(academyId: Long): Academy

    suspend fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson): Lesson
    suspend fun requestDeleteLesson(lessonId: Long): Response<Unit>
    suspend fun createLesson(lesson: Lesson): Lesson
}