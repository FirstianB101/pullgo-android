package com.ich.pullgo.presentation.main.teacher_main.manage_academy.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.common.components.TwoButtonDialog
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.presentation.login.components.startMainActivity
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyState
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.ManageAcademyViewModel
import com.ich.pullgo.presentation.main.teacher_main.manage_academy.manage_people.ManagePeopleActivity
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@Composable
fun TeacherManageAcademyScreen(
    viewModel: ManageAcademyViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    var spinnerState by remember { mutableStateOf(false) }
    var selectAcademyState by remember { mutableStateOf(false) }

    var editState by remember{ mutableStateOf(true) }
    val deleteAcademyDialogState = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val targetState = remember { mutableStateOf(0f) }
    val animatedFloatState = animateFloatAsState(
        targetValue = targetState.value,
        animationSpec = tween(durationMillis = 1500)
    )

    val user = PullgoApplication.instance?.getLoginUser()

    LaunchedEffect(Unit){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ManageAcademyViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ManageAcademyViewModel.UiEvent.DeleteAcademySuccess -> {
                    selectAcademyState = false
                }
                is ManageAcademyViewModel.UiEvent.TransferMainActivityIfAppliedAcademyCnt1To0 -> {
                    startMainActivity(context,user,false)
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    state.value.ownedAcademies.forEach { academy ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.onEvent(ManageAcademyEvent.SelectAcademy(academy))
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

        if (selectAcademyState) {
            targetState.value = 1f
            Column(
                modifier = Modifier
                    .alpha(animatedFloatState.value)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width(80.dp),
                        text = stringResource(R.string.academy_address),
                        color = Color.Black,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        readOnly = editState,
                        value = state.value.academyAddress,
                        onValueChange = { viewModel.onEvent(ManageAcademyEvent.AcademyAddressChanged(it)) },
                        label = { Text(stringResource(R.string.academy_address)) },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width(80.dp),
                        text = stringResource(R.string.phone),
                        color = Color.Black,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = editState,
                        value = state.value.academyPhone,
                        onValueChange = { viewModel.onEvent(ManageAcademyEvent.AcademyPhoneChanged(it)) },
                        label = { Text(stringResource(R.string.phone)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                MainThemeRoundButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if(editState) stringResource(R.string.edit)
                            else stringResource(R.string.edit_success)
                ) {
                    editState = !editState
                    if(!editState) {
                        focusRequester.requestFocus()
                    }else{
                        viewModel.onEvent(ManageAcademyEvent.EditAcademy)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                MainThemeRoundButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.manage_people)
                ) {
                    startManagePeopleActivity(context, state.value.selectedAcademy?.id!!)
                }
                
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(R.color.secondary_color),
                            backgroundColor = Color.White
                        ),
                        onClick = {}
                    ) {
                        Text(
                            text = stringResource(R.string.underlined_delegate),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(R.color.material_700_red),
                            backgroundColor = Color.White
                        ),
                        onClick = {
                            deleteAcademyDialogState.value = true
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.underlined_delete_academy),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    TwoButtonDialog(
        title = stringResource(R.string.underlined_delete_academy),
        content = "${state.value.selectedAcademy?.name} 학원을 삭제하시겠습니까?",
        dialogState = deleteAcademyDialogState,
        cancelText = stringResource(R.string.cancel),
        confirmText = stringResource(R.string.delete),
        onCancel = { deleteAcademyDialogState.value = false },
        onConfirm = {
            viewModel.onEvent(ManageAcademyEvent.DeleteAcademy)
            deleteAcademyDialogState.value = false
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

fun startManagePeopleActivity(context: Context, selectedAcademyId: Long){
    val intent = Intent(context, ManagePeopleActivity::class.java)
    intent.putExtra("selectedAcademyId",selectedAcademyId)

    context.startActivity(intent)
}