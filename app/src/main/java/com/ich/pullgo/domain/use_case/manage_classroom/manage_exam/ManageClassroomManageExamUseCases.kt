package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam

data class ManageClassroomManageExamUseCases(
    val getExamsInClassroom: GetExamsInClassroomUseCase,
    val getFinishedExams: GetFinishedExamsUseCase,
    val getCancelledExams: GetCancelledExamsUseCase,
    val createExam: CreateExamUseCase,
    val editExam: EditExamUseCase,
    val deleteExam: DeleteExamUseCase,
    val finishExam: FinishExamUseCase,
    val cancelExam: CancelExamUseCase,
    val getOneExam: GetOneExamUseCase,
    val getOneStudent: GetOneStudentUseCase,
    val getAttenderStates: GetAttenderStatesInExamUseCase,
    val getStudentsInClassroom: ManageExamGetStudentsInClassroomUseCase
)
