package com.ich.pullgo.data.repository

import com.ich.pullgo.common.util.Constants.MAX_LESSONS
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toAcademy
import com.ich.pullgo.data.remote.dto.toClassroom
import com.ich.pullgo.data.remote.dto.toLesson
import com.ich.pullgo.di.PullgoRetrofitService
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.repository.LessonsRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class LessonsRepositoryImpl @Inject constructor(
    @PullgoRetrofitService private val api: PullgoApi
): LessonsRepository {
    override suspend fun getStudentLessonsOnDate(id: Long, date: String): List<Lesson> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return api.getStudentLessonsByDate(id,date,df.format(cal.time),MAX_LESSONS).map{it.toLesson()}
    }

    override suspend fun getTeacherLessonsOnDate(id: Long, date: String): List<Lesson> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return api.getTeacherLessonsByDate(id,date,df.format(cal.time),MAX_LESSONS).map{it.toLesson()}
    }

    //현재 날짜 앞뒤 30일 레슨들
    override suspend fun getStudentLessonsOnMonth(id: Long): List<Lesson>{
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val today = df.format(cal.time)
        cal.time = df.parse(today)
        cal.add(Calendar.DATE,-30)
        val start = df.format(cal.time)
        cal.add(Calendar.DATE,60)
        val end = df.format(cal.time)
        return api.getStudentLessonsByDate(id,start,end,MAX_LESSONS).map{it.toLesson()}
    }

    override suspend fun getTeacherLessonsOnMonth(id: Long): List<Lesson>{
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val today = df.format(cal.time)
        cal.time = df.parse(today)
        cal.add(Calendar.DATE,-30)
        val start = df.format(cal.time)
        cal.add(Calendar.DATE,60)
        val end = df.format(cal.time)
        return api.getTeacherLessonsByDate(id,start,end,MAX_LESSONS).map{it.toLesson()}
    }

    override suspend fun getClassroomSuchLesson(classroomId: Long) = api.getClassroomById(classroomId).toClassroom()
    override suspend fun getAcademySuchLesson(academyId: Long) = api.getAcademyById(academyId).toAcademy()

    override suspend fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson) = api.patchLessonInfo(lessonId,lesson).toLesson()
    override suspend fun requestDeleteLesson(lessonId: Long) = api.deleteLesson(lessonId)

    override suspend fun createLesson(lesson: Lesson): Lesson = api.createLesson(lesson).toLesson()

    override suspend fun getClassroomsTeacherApplied(teacherId: Long): List<Classroom>
            = api.getClassroomsByTeacherId(teacherId).map{c -> c.toClassroom()}
}