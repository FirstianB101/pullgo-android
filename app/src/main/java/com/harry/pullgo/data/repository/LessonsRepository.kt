package com.harry.pullgo.data.repository

import com.harry.pullgo.data.api.RetrofitClient
import com.harry.pullgo.data.models.Lesson
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LessonsRepository {
    private val lessonClient = RetrofitClient.getApiService()

    suspend fun getStudentLessons(id: Long) = lessonClient.getLessonsByStudentId(id)
    suspend fun getTeacherLessons(id: Long) = lessonClient.getLessonsByTeacherId(id)

    suspend fun getStudentLessonOnDate(id: Long, date: String): Response<List<Lesson>> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return lessonClient.getStudentLessonsByDate(id,date,df.format(cal.time))
    }
    suspend fun getTeacherLessonOnDate(id: Long, date: String): Response<List<Lesson>> {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        cal.time = df.parse(date)
        cal.add(Calendar.DATE,1)
        return lessonClient.getTeacherLessonsByDate(id,date,df.format(cal.time))
    }

    suspend fun getClassroomSuchLesson(classroomId: Long) = lessonClient.getClassroomById(classroomId)
    suspend fun getAcademySuchClassroom(academyId: Long) = lessonClient.getAcademyById(academyId)

    fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson) = lessonClient.patchLessonInfo(lessonId,lesson)
    fun requestDeleteLesson(lessonId: Long) = lessonClient.deleteLesson(lessonId)
}