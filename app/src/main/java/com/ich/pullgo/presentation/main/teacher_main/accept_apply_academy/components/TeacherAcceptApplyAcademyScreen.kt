package com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.components

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.ich.pullgo.R
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyEvent
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyViewModel
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun TeacherAcceptApplyAcademyScreen(
    viewModel: AcceptApplyAcademyViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    var spinnerState by remember { mutableStateOf(false) }
    var selectAcademyState by remember{ mutableStateOf(false) }

    val targetState = remember{ mutableStateOf(0f) }
    val animatedFloatState = animateFloatAsState(
        targetValue = targetState.value,
        animationSpec = tween(durationMillis = 1500)
    )

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is AcceptApplyAcademyViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(selectAcademyState){
        if(selectAcademyState)
            viewModel.onEvent(AcceptApplyAcademyEvent.GetStudentRequests)
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
                                viewModel.onEvent(AcceptApplyAcademyEvent.SelectAcademy(academy))
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
            Box(modifier = Modifier.alpha(animatedFloatState.value)){
                AcceptApplyAcademyTab(
                    viewModel = viewModel
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.value.isLoading){
            CircularProgressIndicator()
        }
    }
}