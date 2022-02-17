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
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyState
import com.ich.pullgo.presentation.main.teacher_main.accept_apply_academy.AcceptApplyAcademyViewModel

@ExperimentalMaterialApi
@Composable
fun TeacherAcceptApplyAcademyScreen(
    viewModel: AcceptApplyAcademyViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    var spinnerState by remember { mutableStateOf(false) }
    var appliedAcademies: List<Academy> by remember { mutableStateOf(emptyList()) }
    var selectedAcademy: Academy? by remember { mutableStateOf(null) }
    var selectAcademyState by remember{ mutableStateOf(false) }

    val targetState = remember{ mutableStateOf(0f) }
    val animatedFloatState = animateFloatAsState(
        targetValue = targetState.value,
        animationSpec = tween(durationMillis = 1500)
    )

    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher

    LaunchedEffect(Unit){
        viewModel.getAppliedAcademies(teacher?.id!!)
    }

    when(state.value){
        is AcceptApplyAcademyState.GetAcademies -> {
            appliedAcademies = (state.value as AcceptApplyAcademyState.GetAcademies).academies
            viewModel.onResultConsume()
        }
        is AcceptApplyAcademyState.Loading -> {
            LoadingScreen()
        }
        is AcceptApplyAcademyState.Error -> {
            Toast.makeText(context, "학원 목록을 불러올 수 없습니다",Toast.LENGTH_SHORT).show()
            viewModel.onResultConsume()
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
            Box(modifier = Modifier.alpha(animatedFloatState.value)){
                AcceptApplyAcademyTab(selectedAcademy!!)
            }
        }
    }
}