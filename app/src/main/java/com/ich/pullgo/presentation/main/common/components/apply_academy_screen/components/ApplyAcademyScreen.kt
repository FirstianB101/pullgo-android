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
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.AcademyItem
import com.ich.pullgo.presentation.login.components.startMainActivity
import com.ich.pullgo.presentation.main.common.components.apply_academy_screen.ApplyAcademyEvent
import com.ich.pullgo.presentation.main.common.components.apply_academy_screen.ApplyAcademyViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun ApplyAcademyScreen(
    isTeacher: Boolean,
    viewModel: ApplyAcademyViewModel = hiltViewModel()
){
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val requestDialogState = remember { mutableStateOf(false) }
    var createAcademyDialogState by remember { mutableStateOf(false) }

    val app = PullgoApplication.instance!!

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ApplyAcademyViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ApplyAcademyViewModel.UiEvent.TransferMainActivityIfAppliedAcademyCnt0To1 -> {
                    if(!app.academyExist) {
                        app.academyExist = true
                        startMainActivity(context, app.getLoginUser(), true)
                    }
                }
            }
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
                value = state.value.searchQuery,
                onValueChange = { viewModel.onEvent(ApplyAcademyEvent.SearchQueryChanged(it)) },
                label = { Text(stringResource(R.string.academy_name)) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(ApplyAcademyEvent.SearchAcademies)
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
            items(state.value.searchedAcademies.size){ idx ->
                AcademyItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            viewModel.onEvent(ApplyAcademyEvent.SelectAcademy(state.value.searchedAcademies[idx]))
                            requestDialogState.value = true
                        },
                    academy = state.value.searchedAcademies[idx],
                    showDeleteButton = false,
                    onDeleteButtonClicked = {}
                )
            }
        }
        if(state.value.searchedAcademies.isEmpty()){
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
        content = "${state.value.selectedAcademy?.name} (학원)에 가입을 신청하시겠습니까?",
        dialogState = requestDialogState,
        cancelText = "취소",
        confirmText = "신청",
        onCancel = { requestDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ApplyAcademyEvent.RequestApplyingAcademy)
            requestDialogState.value = false
        }
    )

    CreateAcademyDialog(
        showDialog = createAcademyDialogState,
        viewModel = viewModel,
        onCreateClicked = {
            viewModel.onEvent(ApplyAcademyEvent.CreateAcademy)
            createAcademyDialogState = false
        },
        onCancelClicked = { createAcademyDialogState = false }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}