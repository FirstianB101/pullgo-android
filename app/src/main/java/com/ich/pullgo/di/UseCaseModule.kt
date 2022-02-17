package com.ich.pullgo.di

import com.ich.pullgo.domain.repository.*
import com.ich.pullgo.domain.use_case.accept_apply_academy.*
import com.ich.pullgo.domain.use_case.apply_academy.*
import com.ich.pullgo.domain.use_case.apply_classroom.*
import com.ich.pullgo.domain.use_case.apply_classroom.GetTeacherAppliedAcademiesUseCase
import com.ich.pullgo.domain.use_case.calendar.*
import com.ich.pullgo.domain.use_case.change_info.ChangeInfoUseCases
import com.ich.pullgo.domain.use_case.change_info.ChangeStudentInfoUseCase
import com.ich.pullgo.domain.use_case.change_info.ChangeTeacherInfoUseCase
import com.ich.pullgo.domain.use_case.change_info.PwCheckUseCase
import com.ich.pullgo.domain.use_case.exam_history.ExamHistoryUseCases
import com.ich.pullgo.domain.use_case.exam_history.GetAttenderAnswersUseCase
import com.ich.pullgo.domain.use_case.exam_history.GetQuestionsForHistoryUseCase
import com.ich.pullgo.domain.use_case.exam_list.*
import com.ich.pullgo.domain.use_case.manage_academy.*
import com.ich.pullgo.domain.use_case.manage_classroom.CreateClassroomUseCase
import com.ich.pullgo.domain.use_case.manage_classroom.GetAppliedAcademiesUseCase
import com.ich.pullgo.domain.use_case.manage_classroom.GetClassroomsTeacherAppliedUseCase
import com.ich.pullgo.domain.use_case.manage_classroom.ManageClassroomUseCases
import com.ich.pullgo.domain.use_case.manage_classroom.edit_classroom.DeleteClassroomUseCase
import com.ich.pullgo.domain.use_case.manage_classroom.edit_classroom.EditClassroomUseCase
import com.ich.pullgo.domain.use_case.manage_classroom.edit_classroom.ManageClassroomEditClassroomUseCases
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.*
import com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.manage_question.*
import com.ich.pullgo.domain.use_case.manage_classroom.manage_people.*
import com.ich.pullgo.domain.use_case.manage_classroom.manage_request.*
import com.ich.pullgo.domain.use_case.manage_request.*
import com.ich.pullgo.domain.use_case.sign_up.CheckIdExistUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateStudentUseCase
import com.ich.pullgo.domain.use_case.sign_up.CreateTeacherUseCase
import com.ich.pullgo.domain.use_case.sign_up.SignUpUseCases
import com.ich.pullgo.domain.use_case.take_exam.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSignUpUseCases(repository: SignUpRepository): SignUpUseCases{
        return SignUpUseCases(
            createStudent = CreateStudentUseCase(repository),
            createTeacher = CreateTeacherUseCase(repository),
            checkIdExist = CheckIdExistUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideChangeInfoUseCases(repository: ChangeInfoRepository): ChangeInfoUseCases{
        return ChangeInfoUseCases(
            changeStudentInfo = ChangeStudentInfoUseCase(repository),
            changeTeacherInfo = ChangeTeacherInfoUseCase(repository),
            checkUserPassword = PwCheckUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCalendarWithLessonsUseCases(repository: LessonsRepository): CalendarWithLessonUseCases{
        return CalendarWithLessonUseCases(
            getAcademySuchLesson = GetAcademySuchLessonUseCase(repository),
            getClassroomSuchLesson = GetClassroomSuchLessonUseCase(repository),
            getStudentLessonsOnDate = GetStudentLessonsOnDateUseCase(repository),
            getStudentLessonsOnMonth = GetStudentLessonsOnMonthUseCase(repository),
            getTeacherLessonsOnDate = GetTeacherLessonsOnDateUseCase(repository),
            getTeacherLessonsOnMonth = GetTeacherLessonsOnMonthUseCase(repository),
            getClassroomsTeacherApplied = GetTeacherClassroomsUseCase(repository),
            patchLessonInfo = PatchLessonInfoUseCase(repository),
            deleteLesson = DeleteLessonUseCase(repository),
            createLesson = CreateLessonUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageRequestUseCases(repository: ManageRequestRepository): ManageRequestUseCases{
        return ManageRequestUseCases(
            getAcademiesStudentApplying = GetAcademiesStudentApplyingUseCase(repository),
            getAcademiesTeacherApplying = GetAcademiesTeacherApplyingUseCase(repository),
            getClassroomsStudentApplying = GetClassroomsStudentApplyingUseCase(repository),
            getClassroomsTeacherApplying = GetClassroomsTeacherApplyingUseCase(repository),
            removeStudentApplyingAcademyRequest = RemoveStudentAcademyRequestUseCase(repository),
            removeStudentApplyingClassroomRequest = RemoveStudentClassroomRequestUseCase(repository),
            removeTeacherApplyingAcademyRequest = RemoveTeacherAcademyRequestUseCase(repository),
            removeTeacherApplyingClassroomRequest = RemoveTeacherClassroomRequestUseCase(repository),
            getAcademyOfClassroom = GetAcademyOfClassroomUseCase(repository),
            getOwnerOfAcademy = GetOwnerOfAcademyUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideApplyClassroomUseCases(repository: ApplyClassroomRepository): ApplyClassroomUseCases{
        return ApplyClassroomUseCases(
            getSearchedClassrooms = GetSearchedClassroomsUseCase(repository),
            getStudentAppliedAcademies = GetStudentAppliedAcademiesUseCase(repository),
            getTeacherAppliedAcademies = GetTeacherAppliedAcademiesUseCase(repository),
            sendStudentApplyClassroomRequest = SendStudentApplyClassroomRequestUseCase(repository),
            sendTeacherApplyClassroomRequest = SendTeacherApplyClassroomRequestUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAcceptApplyAcademyUseCases(repository: AcceptApplyAcademyRepository): AcceptApplyAcademyUseCases{
        return AcceptApplyAcademyUseCases(
            getAcademiesTeacherApplied = com.ich.pullgo.domain.use_case.accept_apply_academy.GetTeacherAppliedAcademiesUseCase(repository),
            getStudentsApplyingAcademy = GetStudentsApplyingAcademyUseCase(repository),
            getTeachersApplyingAcademy = GetTeachersApplyingAcademyUseCase(repository),
            acceptStudentRequest = AcceptStudentApplyingAcademyUseCase(repository),
            acceptTeacherRequest = AcceptTeacherApplyingAcademyUseCase(repository),
            denyStudentRequest = DenyStudentApplyingAcademyUseCase(repository),
            denyTeacherRequest = DenyTeacherApplyingAcademyUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideApplyAcademyUseCases(repository: ApplyAcademyRepository): ApplyAcademyUseCases{
        return ApplyAcademyUseCases(
            createAcademy = CreateAcademyUseCase(repository),
            getSearchedAcademies = GetSearchedAcademiesUseCase(repository),
            studentApplyAcademy = StudentApplyAcademyUseCase(repository),
            teacherApplyAcademy = TeacherApplyAcademyUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageAcademyUseCases(repository: ManageAcademyRepository): ManageAcademyUseCases{
        return ManageAcademyUseCases(
            getOwnedAcademies = GetOwnedAcademiesUseCase(repository),
            editAcademy = EditAcademyUseCase(repository),
            deleteAcademy = DeleteAcademyUseCase(repository),
            getStudents = GetStudentsInAcademyUseCase(repository),
            getTeachers = GetTeachersInAcademyUseCase(repository),
            kickStudent = KickStudentUseCase(repository),
            kickTeacher = KickTeacherUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageClassroomUseCases(repository: ManageClassroomRepository): ManageClassroomUseCases{
        return ManageClassroomUseCases(
            createClassroom = CreateClassroomUseCase(repository),
            getClassroomsTeacherApplied = GetClassroomsTeacherAppliedUseCase(repository),
            getAppliedAcademies = GetAppliedAcademiesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageClassroomEditClassroomUseCases(repository: ManageClassroomRepository): ManageClassroomEditClassroomUseCases{
        return ManageClassroomEditClassroomUseCases(
            editClassroom = EditClassroomUseCase(repository),
            deleteClassroom = DeleteClassroomUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageClassroomManagePeopleUseCases(repository: ManageClassroomRepository): ManageClassroomManagePeopleUseCases{
        return ManageClassroomManagePeopleUseCases(
            getStudents = GetStudentsInClassroomUseCase(repository),
            getTeachers = GetTeachersInClassroomUseCase(repository),
            kickStudent = KickStudentInClassroomUseCase(repository),
            kickTeacher = KickTeacherInClassroomUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageClassroomManageRequestUseCases(repository: ManageClassroomRepository): ManageClassroomManageRequestUseCases{
        return ManageClassroomManageRequestUseCases(
            getStudentRequests = GetStudentClassroomRequestsUseCase(repository),
            getTeacherRequests = GetTeacherClassroomRequestsUseCase(repository),
            acceptStudent = AcceptStudentClassroomRequestUseCase(repository),
            acceptTeacher = AcceptTeacherClassroomRequestUseCase(repository),
            denyStudent = DenyStudentClassroomRequestUseCase(repository),
            denyTeacher = DenyTeacherClassroomRequestUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageClassroomManageExamUseCases(repository: ManageClassroomRepository): ManageClassroomManageExamUseCases{
        return ManageClassroomManageExamUseCases(
            getExamsInClassroom = GetExamsInClassroomUseCase(repository),
            getFinishedExams = GetFinishedExamsUseCase(repository),
            getCancelledExams = GetCancelledExamsUseCase(repository),
            getOneExam = GetOneExamUseCase(repository),
            createExam = CreateExamUseCase(repository),
            cancelExam = CancelExamUseCase(repository),
            editExam = EditExamUseCase(repository),
            deleteExam = DeleteExamUseCase(repository),
            finishExam = FinishExamUseCase(repository),
            getOneStudent = GetOneStudentUseCase(repository),
            getAttenderStates = GetAttenderStatesInExamUseCase(repository),
            getStudentsInClassroom = ManageExamGetStudentsInClassroomUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideManageQuestionUseCases(repository: ManageQuestionRepository): ManageQuestionUseCases{
        return ManageQuestionUseCases(
            createQuestion = CreateQuestionUseCase(repository),
            deleteQuestion = DeleteQuestionUseCase(repository),
            editQuestion = EditQuestionUseCase(repository),
            getQuestions = GetQuestionsInExamUseCase(repository),
            uploadImage = UploadQuestionImageUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideStudentExamListUseCases(repository: ExamsRepository): StudentExamListUseCases{
        return StudentExamListUseCases(
            getExamsByName = GetExamsByNameUseCase(repository),
            getExamsByBeginDate = GetExamsByBeginDateUseCase(repository),
            getExamsByEndDate = GetExamsByEndDateUseCase(repository),
            getExamsByEndDateDesc = GetExamsByEndDateDescUseCase(repository),
            getStatesByStudentId = GetStatesByStudentIdUseCase(repository),
            startTakingExam = StartTakingExamUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideExamHistoryUseCases(repository: ExamHistoryRepository): ExamHistoryUseCases{
        return ExamHistoryUseCases(
            getQuestions = GetQuestionsForHistoryUseCase(repository),
            getAttenderAnswers = GetAttenderAnswersUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideTakeExamUseCases(repository: TakeExamRepository): TakeExamUseCases{
        return TakeExamUseCases(
            getAttenderAnswers = GetAttenderAnswersUseCase(repository),
            getAnAttenderState = GetAnAttenderStateUseCase(repository),
            getQuestionsInExam = GetQuestionsForExamUseCase(repository),
            saveAttenderAnswer = SaveAttenderAnswerUseCase(repository),
            submitAttenderState = SubmitAttenderStateUseCase(repository)
        )
    }
}