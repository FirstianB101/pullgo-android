package com.ich.pullgo.data.repository

import com.ich.pullgo.data.remote.dto.Schedule
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.Lesson
import com.ich.pullgo.domain.repository.LessonsRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeCalendarLessonRepository: LessonsRepository {

    val LESSON_ID1 = 1L
    val LESSON_ID2 = 2L
    val LESSON_ID3 = 3L
    val LESSON_ID4 = 4L
    val CLASSROOM_ID1 = 5L
    val CLASSROOM_ID2 = 6L
    val STUDENT_ID = 7L
    val TEACHER_ID = 8L
    val ACADEMY_ID = 9L

    val lessons = mutableListOf<Lesson>()
    val classrooms = mutableListOf<Classroom>()

    init {
        lessons.add(
            Lesson("test_lesson1",null, Schedule("2022-02-22","16:00:00","17:00:00"),null)
                .also{it.id = LESSON_ID1}
        )
        lessons.add(
            Lesson("test_lesson2",null, Schedule("2022-02-22","17:00:00","18:00:00"),null)
                .also{it.id = LESSON_ID2}
        )
        lessons.add(
            Lesson("test_lesson3",null, Schedule("2022-02-23","18:00:00","19:00:00"),null)
                .also{it.id = LESSON_ID3}
        )
        lessons.add(
            Lesson("test_lesson4",null, Schedule("2022-02-24","19:00:00","20:00:00"),null)
                .also{it.id = LESSON_ID4}
        )

        classrooms.add(
            Classroom(ACADEMY_ID,"test_classroom1",null).also{it.id = CLASSROOM_ID1}
        )
        classrooms.add(
            Classroom(ACADEMY_ID,"test_classroom2",null).also{it.id = CLASSROOM_ID2}
        )
    }

    override suspend fun getStudentLessonsOnDate(id: Long, date: String): List<Lesson> {
        return lessons.filter { it.schedule?.date == date }
    }

    override suspend fun getTeacherLessonsOnDate(id: Long, date: String): List<Lesson> {
        return lessons.filter { it.schedule?.date == date }
    }

    override suspend fun getStudentLessonsOnMonth(id: Long): List<Lesson> {
        return lessons
    }

    override suspend fun getTeacherLessonsOnMonth(id: Long): List<Lesson> {
        return lessons
    }

    override suspend fun getClassroomsTeacherApplied(teacherId: Long): List<Classroom> {
        return classrooms
    }

    override suspend fun getClassroomSuchLesson(classroomId: Long): Classroom {
        when(classroomId){
            CLASSROOM_ID1 -> return classrooms[0]
            CLASSROOM_ID2 -> return classrooms[1]
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }

    override suspend fun getAcademySuchLesson(academyId: Long): Academy {
        return Academy("test_academy","","",null).also { it.id = ACADEMY_ID }
    }

    override suspend fun requestPatchLessonInfo(lessonId: Long, lesson: Lesson): Lesson {
        for(les in lessons){
            if(les.id == lessonId){
                les.name = lesson.name
                les.academyId = lesson.academyId
                les.classroomId = lesson.classroomId
                les.schedule = lesson.schedule
                return les
            }
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }

    override suspend fun requestDeleteLesson(lessonId: Long): Response<Unit> {
        for(lesson in lessons){
            if(lesson.id == lessonId){
                return Response.success(Unit)
            }
        }
        throw HttpException(Response.error<ResponseBody>(500 , ResponseBody.create("plain/text".toMediaTypeOrNull(),"some content")))
    }

    override suspend fun createLesson(lesson: Lesson): Lesson {
        lessons.add(lesson)
        return lesson
    }
}