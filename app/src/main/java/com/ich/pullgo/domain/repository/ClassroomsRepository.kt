package com.ich.pullgo.domain.repository

import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson

interface ClassroomsRepository{
    suspend fun getClassroomById(classroomId: Long): Classroom
    suspend fun getClassroomsByTeacherId(id: Long): List<Classroom>
    suspend fun getAcademiesByTeacherId(id: Long): List<Academy>

    suspend fun createNewLesson(lesson: Lesson): Lesson
}