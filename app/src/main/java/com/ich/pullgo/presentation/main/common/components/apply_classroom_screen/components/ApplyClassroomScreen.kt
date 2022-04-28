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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.common.components.items.ClassroomItem
import com.ich.pullgo.presentation.main.common.components.apply_classroom_screen.ApplyClassroomEvent
import com.ich.pullgo.presentation.main.common.components.apply_classroom_screen.ApplyClassroomViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun ApplyClassroomScreen(
    viewModel: ApplyClassroomViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    var spinnerState by rememberSaveable { mutableStateOf(false) }

    var selectAcademyState by remember { mutableStateOf(false) }
    var requestDialogState = remember { mutableStateOf(false) }

    val targetState = rememberSaveable { mutableStateOf(0f) }
    val animatedFloatState = animateFloatAsState(
        targetValue = targetState.value,
        animationSpec = tween(durationMillis = 1500)
    )

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ApplyClassroomViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ApplyClassroomViewModel.UiEvent.CloseRequestDialog -> {
                    requestDialogState.value = false
                }
            }
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
                modifier = Modifier.fillMaxWidth(),
                expanded = spinnerState,
                onExpandedChange = {
                    spinnerState = !spinnerState
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = state.value.selectedAcademy?.name ?: "",
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
                    state.value.appliedAcademies.forEach { academy ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.onEvent(ApplyClassroomEvent.SelectAcademy(academy))
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
                        value = state.value.searchQuery,
                        onValueChange = { viewModel.onEvent(ApplyClassroomEvent.SearchQueryChanged(it)) },
                        label = { Text(stringResource(R.string.search_by_teacher_or_classroom_name)) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(ApplyClassroomEvent.SearchClassrooms)
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
                    items(state.value.searchedClassrooms.size){ idx ->
                        ClassroomItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    viewModel.onEvent(
                                        ApplyClassroomEvent.SelectClassroom(state.value.searchedClassrooms[idx])
                                    )
                                    requestDialogState.value = true
                                },
                            classroom = state.value.searchedClassrooms[idx],
                            showDeleteButton = false,
                            onDeleteButtonClicked = {}
                        )
                    }
                }
                if(state.value.searchedClassrooms.isEmpty()){
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
        content = "${state.value.selectedClassroom?.name?.split(';')?.get(0)} 반에 가입을 신청하시겠습니까?",
        dialogState = requestDialogState,
        cancelText = "취소",
        confirmText = "신청",
        onCancel = { requestDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ApplyClassroomEvent.RequestApplyingClassroom)
        }
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