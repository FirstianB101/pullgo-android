package com.ich.pullgo.data.repository

import com.ich.pullgo.common.Constants.MAX_LESSONS
import com.ich.pullgo.data.remote.PullgoApi
import com.ich.pullgo.data.remote.dto.toLesson
import com.ich.pullgo.di.PullgoRetrofitApi
import com.ich.pullgo.domain.model.Lesson
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LessonsRepositoryImpl @Inject constructor(
    @PullgoRetrofitApi private val api: PullgoApi
) {

    suspend fun getStudentLessonsOnDate(id: Long, date: String): List<Lesson> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return api.getStudentLessonsByDate(id,date,df.format(cal.time),MAX_LESSONS).map{it.toLesson()}
    }

    suspend fun getTeacherLessonsOnDate(id: Long, date: String): List<Lesson> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return api.getTeacherLessonsByDate(id,date,df.format(cal.time),MAX_LESSONS).map{it.toLesson()}
    }

    //현재 날짜 앞뒤 30일 레슨들
    suspend fun getStudentLessonsOnMonth(id: Long): List<Lesson>{
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

    suspend fun getTeacherLessonsOnMonth(id: Long): List<Lesson>{
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

    suspend fun getClassroomSuchLesson(classroomId: Long) = api.getClassroomById(classroomId)
    suspend fun getAcademySuchClassroom(academyId: Long) = api.getAcademyById(academyId)

    suspend fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson) = api.patchLessonInfo(lessonId,lesson)
    suspend fun requestDeleteLesson(lessonId: Long) = api.deleteLesson(lessonId)
}