package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson
import retrofit2.Response

interface LessonsRepository {

    suspend fun getStudentLessonsOnDate(id: Long, date: String): Response<List<Lesson>>
    suspend fun getTeacherLessonsOnDate(id: Long, date: String): Response<List<Lesson>>
    suspend fun getStudentLessonsOnMonth(id: Long): Response<List<Lesson>>
    suspend fun getTeacherLessonsOnMonth(id: Long): Response<List<Lesson>>

    suspend fun getClassroomSuchLesson(classroomId: Long): Classroom
    suspend fun getAcademySuchClassroom(academyId: Long): Academy

    suspend fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson): Lesson
    suspend fun requestDeleteLesson(lessonId: Long)
}