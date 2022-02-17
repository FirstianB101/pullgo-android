package com.ich.pullgo.presentation.main.common.components.apply_academy_screen.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.ich.pullgo.common.components.items.AcademyItem
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.doJob
import com.ich.pullgo.presentation.login.components.startMainActivity
import com.ich.pullgo.presentation.main.common.components.apply_academy_screen.ApplyAcademyState
import com.ich.pullgo.presentation.main.common.components.apply_academy_screen.ApplyAcademyViewModel

@ExperimentalComposeUiApi
@Composable
fun ApplyAcademyScreen(
    isTeacher: Boolean,
    viewModel: ApplyAcademyViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    var searchText by remember{ mutableStateOf("") }

    var searchedAcademies: List<Academy?> by remember { mutableStateOf(listOf(null)) }
    var selectedAcademy: Academy? by remember { mutableStateOf(null) }

    val requestDialogState = remember { mutableStateOf(false) }
    var createAcademyDialogState by remember { mutableStateOf(false) }

    val user = PullgoApplication.instance?.getLoginUser()
    val app = PullgoApplication.instance!!

    when(state.value){
        is ApplyAcademyState.GetSearchedAcademies -> {
            searchedAcademies = (state.value as ApplyAcademyState.GetSearchedAcademies).academies
            viewModel.onResultConsume()
        }
        is ApplyAcademyState.Loading -> {
            LoadingScreen()
        }
        is ApplyAcademyState.RequestApplyAcademy -> {
            Toast.makeText(context,"가입 신청에 성공하였습니다",Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is ApplyAcademyState.Error -> {
            Toast.makeText(context, (state.value as ApplyAcademyState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
        }
        is ApplyAcademyState.CreateAcademy -> {
            Toast.makeText(context,"학원을 생성하였습니다",Toast.LENGTH_SHORT).show()
            if(!app.academyExist) {
                app.academyExist = true
                startMainActivity(context, user, true)
            }
            viewModel.onResultConsume()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                label = { Text(stringResource(R.string.academy_name)) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.searchAcademy(searchText)
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
            items(searchedAcademies.size){ idx ->
                searchedAcademies[idx]?.let {
                    AcademyItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedAcademy = searchedAcademies[idx]
                                requestDialogState.value = true
                            },
                        academy = searchedAcademies[idx]!!,
                        showDeleteButton = false,
                        onDeleteButtonClicked = {}
                    )
                }
            }
        }
        if(searchedAcademies.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "검색된 학원이 없습니다",
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

    if(isTeacher){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    createAcademyDialogState = true
                },
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }

    TwoButtonDialog(
        title = "학원 가입 요청",
        content = "${selectedAcademy?.name} (학원)에 가입을 신청하시겠습니까?",
        dialogState = requestDialogState,
        cancelText = "취소",
        confirmText = "신청",
        onCancel = { requestDialogState.value = false },
        onConfirm = {
            user?.doJob(
                ifStudent = { viewModel.requestStudentApplyAcademy(user.student!!.id!!,selectedAcademy?.id!!) },
                ifTeacher = { viewModel.requestTeacherApplyAcademy(user.teacher!!.id!!,selectedAcademy?.id!!) }
            )
            requestDialogState.value = false
        }
    )

    CreateAcademyDialog(
        showDialog = createAcademyDialogState,
        onCreateClicked = { newAcademy ->
            viewModel.createAcademy(newAcademy)
            createAcademyDialogState = false
        },
        onCancelClicked = { createAcademyDialogState = false }
    )
}