package com.ich.pullgo.presentation.main.student_main.exam_history_screen.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ExamItem
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Exam
import com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_history.ExamHistoryActivity
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.StudentExamListState
import com.ich.pullgo.presentation.main.student_main.exam_list_screen.StudentExamListViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentExamHistoryScreen(
    viewModel: StudentExamListViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val exams: MutableState<List<Exam>> = remember { mutableStateOf(emptyList()) }
    val attenderStates: MutableState<List<AttenderState>> = remember { mutableStateOf(emptyList()) }
    val takenExams: MutableState<List<Exam?>> = remember { mutableStateOf(listOf(null)) }
    var selectedExam: Exam? by remember{ mutableStateOf(null) }

    var spinnerExpanded by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf("이름 순") }
    val options = listOf(
        stringResource(R.string.sort_exam_by_name),
        stringResource(R.string.sort_exam_by_begin_date),
        stringResource(R.string.sort_exam_by_end_date)
    )

    val showDialogState = remember { mutableStateOf(false) }

    val student = PullgoApplication.instance?.getLoginUser()?.student

    LaunchedEffect(Unit){
        viewModel.getExamsByName(student?.id!!)
    }

    fun gatherTakenExam(){
        val taken = mutableListOf<Exam>()
        val checkMap = mutableMapOf<Long,Boolean>()

        for(take in attenderStates.value) {
            if(take.progress == Constants.ATTENDER_PROGRESS_COMPLETE) {
                checkMap[take.examId!!] = true
            }
        }

        for(exam in exams.value){
            if(checkMap[exam.id!!] == true) {
                taken.add(exam)
            }
        }
        takenExams.value = taken
    }

    when(state.value){
        is StudentExamListState.GetExams -> {
            exams.value = (state.value as StudentExamListState.GetExams).exams
            viewModel.getStatesByStudentId(student?.id!!)
        }
        is StudentExamListState.GetStates -> {
            attenderStates.value = (state.value as StudentExamListState.GetStates).states
            gatherTakenExam()
            viewModel.onResultConsume()
        }
        is StudentExamListState.Error -> {
            Toast.makeText(context,(state.value as StudentExamListState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is StudentExamListState.Loading -> {
            LoadingScreen()
        }
    }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(8.dp),
            expanded = spinnerExpanded,
            onExpandedChange = {
                spinnerExpanded = !spinnerExpanded
            }
        ) {
            TextButton(
                onClick = {
                    spinnerExpanded = true
                }
            ){
                Text(
                    text = sortOption,
                    color = Color.Black
                )
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = spinnerExpanded
                )
            }
            ExposedDropdownMenu(
                expanded = spinnerExpanded,
                onDismissRequest = {
                    spinnerExpanded = false
                }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            sortOption = option
                            spinnerExpanded = false

                            when(option){
                                "이름 순" -> viewModel.getExamsByName(student?.id!!)
                                "시작날짜 순" -> viewModel.getExamsByBeginDate(student?.id!!)
                                "종료날짜 순" -> viewModel.getExamsByEndDateDesc(student?.id!!)
                            }
                        }
                    ) {
                        Text(
                            text = option,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        LazyColumn{
            items(takenExams.value.size){ idx ->
                takenExams.value[idx]?.let {
                    ExamItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedExam = it
                                showDialogState.value = true
                            },
                        exam = it,
                        showDeleteButton = false,
                        onDeleteButtonClicked = {}
                    )
                }
            }
        }
        if(takenExams.value.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "응시한 시험이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
        if(state.value is StudentExamListState.Error){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "정보를 불러올 수 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

    TwoButtonDialog(
        title = stringResource(R.string.taken_exam),
        content = "${selectedExam?.name} 시험을 확인하시겠습니까?",
        dialogState = showDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.show_history),
        onCancel = { showDialogState.value = false },
        onConfirm = {
            for(attenderState in attenderStates.value){
                if(selectedExam?.id == attenderState.examId){
                    val intent = Intent(context, ExamHistoryActivity::class.java)
                    intent.putExtra("selectedExam",selectedExam)
                    intent.putExtra("attenderState",attenderState)
                    context.startActivity(intent)
                    break
                }
            }
            showDialogState.value = false
        }
    )
}