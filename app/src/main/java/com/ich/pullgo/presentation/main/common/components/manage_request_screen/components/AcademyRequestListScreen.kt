package com.ich.pullgo.presentation.main.common.components.manage_request_screen.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.AcademyItem
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestEvent
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalComposeUiApi
@Composable
fun AcademyRequestListScreen(
    viewModel: ManageRequestViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val state = viewModel.state.collectAsState()

    val removeDialogState = remember{ mutableStateOf(false) }
    val academyInfoDialogState = remember{ mutableStateOf(false)}

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManageRequestViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ManageRequestViewModel.UiEvent.CloseRemoveDialog -> {
                    removeDialogState.value = false
                }
            }
        }
    }

    LazyColumn{
        items(state.value.academyRequests.size){ idx ->
            AcademyItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        viewModel.onEvent(ManageRequestEvent.SelectAcademy(state.value.academyRequests[idx]))
                        viewModel.onEvent(ManageRequestEvent.ShowAcademyInfo)
                        academyInfoDialogState.value = true
                    },
                academy = state.value.academyRequests[idx],
                showDeleteButton = true
            ){
                viewModel.onEvent(ManageRequestEvent.SelectAcademy(state.value.academyRequests[idx]))
                removeDialogState.value = true
            }
        }
    }
    if(state.value.academyRequests.isEmpty()){
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "보낸 학원 가입 요청이 없습니다",
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }

    TwoButtonDialog(
        title = "요청 삭제",
        content = "${state.value.selectedAcademy?.name} 가입 요청을 취소하시겠습니까?",
        dialogState = removeDialogState,
        cancelText = "취소",
        confirmText = "삭제",
        onCancel = { removeDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageRequestEvent.RemoveAcademyRequest)
        }
    )

    AcademyInfoDialog(
        showDialog = academyInfoDialogState,
        academy = state.value.selectedAcademy,
        viewModel = viewModel
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