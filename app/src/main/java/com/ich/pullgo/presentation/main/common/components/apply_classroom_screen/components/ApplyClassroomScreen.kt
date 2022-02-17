package com.ich.pullgo.presentation.main.common.components.apply_classroom_screen.components

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.R
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ClassroomItem
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.presentation.main.common.components.apply_classroom_screen.ApplyClassroomState
import com.ich.pullgo.presentation.main.common.components.apply_classroom_screen.ApplyClassroomViewModel

@ExperimentalMaterialApi
@Composable
fun ApplyClassroomScreen(
    viewModel: ApplyClassroomViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    var spinnerState by remember { mutableStateOf(false) }
    var appliedAcademies: List<Academy> by remember { mutableStateOf(emptyList()) }
    var selectedAcademy: Academy? by remember { mutableStateOf(null) }
    var searchedClassrooms: List<Classroom?> by remember { mutableStateOf(listOf(null))}
    var selectedClassroom: Classroom? by remember{ mutableStateOf(null) }

    var selectAcademyState by remember{ mutableStateOf(false) }
    var requestDialogState = remember{ mutableStateOf(false) }
    var searchText by remember{ mutableStateOf("") }

    val targetState = remember{ mutableStateOf(0f) }
    val animatedFloatState = animateFloatAsState(
        targetValue = targetState.value,
        animationSpec = tween(durationMillis = 1500)
    )

    val user = PullgoApplication.instance?.getLoginUser()

    LaunchedEffect(Unit){
        user?.doJob(
            ifStudent = { viewModel.getStudentAppliedAcademies(user.student?.id!!) },
            ifTeacher = { viewModel.getTeacherAppliedAcademies(user.teacher?.id!!) }
        )
    }

    when(state.value){
        is ApplyClassroomState.AppliedAcademies -> {
            appliedAcademies = (state.value as ApplyClassroomState.AppliedAcademies).academies
        }
        is ApplyClassroomState.SearchedClassrooms -> {
            searchedClassrooms = (state.value as ApplyClassroomState.SearchedClassrooms).classrooms
        }
        is ApplyClassroomState.SendApplyClassroomRequest -> {
            Toast.makeText(context,"가입 요청을 보냈습니다",Toast.LENGTH_SHORT).show()
            requestDialogState.value = false
        }
        is ApplyClassroomState.Loading -> {
            LoadingScreen()
        }
        is ApplyClassroomState.Error -> {
            Toast.makeText(context, (state.value as ApplyClassroomState.Error).message,Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.width(80.dp),
                text = stringResource(R.string.select_academy),
                color = Color.Black,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(20.dp))

            ExposedDropdownMenuBox(
                expanded = spinnerState,
                onExpandedChange = {
                    spinnerState = !spinnerState
                }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedAcademy?.name ?: "",
                    onValueChange = { },
                    label = { Text(stringResource(R.string.select_academy)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = spinnerState
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = spinnerState,
                    onDismissRequest = {
                        spinnerState = false
                    }
                ) {
                    appliedAcademies.forEach { academy ->
                        DropdownMenuItem(
                            onClick = {
                                selectedAcademy = academy
                                selectAcademyState = true
                                spinnerState = false
                            }
                        ) {
                            Text(academy.name ?: "")
                        }
                    }
                }
            }
        }

        if(selectAcademyState){
            targetState.value = 1f
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(animatedFloatState.value)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text(stringResource(R.string.search_by_teacher_or_classroom_name)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.getSearchedClassrooms(selectedAcademy?.id!!,searchText)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }

                LazyColumn{
                    items(searchedClassrooms.size){ idx ->
                        searchedClassrooms[idx]?.let {
                            ClassroomItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        selectedClassroom = searchedClassrooms[idx]
                                        requestDialogState.value = true
                                    },
                                classroom = searchedClassrooms[idx]!!,
                                showDeleteButton = false,
                                onDeleteButtonClicked = {}
                            )
                        }
                    }
                }
                if(searchedClassrooms.isEmpty()){
                    Box(modifier = Modifier.fillMaxSize()){
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "검색된 반이 없습니다",
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }

    TwoButtonDialog(
        title = "반 가입 요청",
        content = "${selectedClassroom?.name?.split(';')?.get(0)} 반에 가입을 신청하시겠습니까?",
        dialogState = requestDialogState,
        cancelText = "취소",
        confirmText = "신청",
        onCancel = { requestDialogState.value = false },
        onConfirm = {
            user?.doJob(
                ifStudent = { viewModel.sendStudentApplyClassroomRequest(user.student?.id!!,selectedClassroom?.id!!) },
                ifTeacher = { viewModel.sendTeacherApplyClassroomRequest(user.teacher?.id!!,selectedClassroom?.id!!) }
            )
        }
    )
}