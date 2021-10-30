package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.PullgoService
import com.harry.pullgo.data.models.Lesson
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonsRepository @Inject constructor(
    private val lessonClient: PullgoService
) {
    val MAX_LESSONS = 100

    suspend fun getStudentLessonsOnDate(id: Long, date: String): Response<List<Lesson>> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return lessonClient.getStudentLessonsByDate(id,date,df.format(cal.time),MAX_LESSONS)
    }

    suspend fun getTeacherLessonsOnDate(id: Long, date: String): Response<List<Lesson>> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return lessonClient.getTeacherLessonsByDate(id,date,df.format(cal.time),MAX_LESSONS)
    }

    //현재 날짜 앞뒤 30일 레슨들
    suspend fun getStudentLessonsOnMonth(id: Long): Response<List<Lesson>>{
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val today = df.format(cal.time)
        cal.time = df.parse(today)
        cal.add(Calendar.DATE,-30)
        val start = df.format(cal.time)
        cal.add(Calendar.DATE,60)
        val end = df.format(cal.time)
        return lessonClient.getStudentLessonsByDate(id,start,end,MAX_LESSONS)
    }

    suspend fun getTeacherLessonsOnMonth(id: Long): Response<List<Lesson>>{
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val today = df.format(cal.time)
        cal.time = df.parse(today)
        cal.add(Calendar.DATE,-30)
        val start = df.format(cal.time)
        cal.add(Calendar.DATE,60)
        val end = df.format(cal.time)
        return lessonClient.getTeacherLessonsByDate(id,start,end,MAX_LESSONS)
    }

    suspend fun getClassroomSuchLesson(classroomId: Long) = lessonClient.getClassroomById(classroomId)
    suspend fun getAcademySuchClassroom(academyId: Long) = lessonClient.getAcademyById(academyId)

    suspend fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson) = lessonClient.patchLessonInfo(lessonId,lesson)
    suspend fun requestDeleteLesson(lessonId: Long) = lessonClient.deleteLesson(lessonId)
}