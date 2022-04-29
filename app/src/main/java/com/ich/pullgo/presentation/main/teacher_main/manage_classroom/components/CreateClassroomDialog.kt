package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.LoadingScreen
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomEvent
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomState
import com.ich.pullgo.presentation.main.teacher_main.manage_classroom.ManageClassroomViewModel
import java.util.*

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CreateClassroomDialog(
    showDialog: MutableState<Boolean>,
    viewModel: ManageClassroomViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    var spinnerState by rememberSaveable { mutableStateOf(false) }
    var selectAcademyState by rememberSaveable { mutableStateOf(false) }

    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
                selectAcademyState = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            ),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(25.dp)
                        .fillMaxWidth()
                        .background(
                            Color.White, RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
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
                            expanded = spinnerState,
                            onExpandedChange = {
                                spinnerState = !spinnerState
                            }
                        ) {
                            OutlinedTextField(
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
                                            viewModel.onEvent(ManageClassroomEvent.SelectAcademy(academy))
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
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.width(80.dp),
                                text = stringResource(R.string.classroom_name),
                                color = Color.Black,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            OutlinedTextField(
                                modifier = Modifier.width(260.dp),
                                value = state.value.newClassroomName,
                                onValueChange = { viewModel.onEvent(ManageClassroomEvent.NewClassroomNameChanged(it)) },
                                label = { Text(text = stringResource(R.string.comment_input_classroom_name)) }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "수업 요일 선택",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        AndroidView(
                            modifier = Modifier.fillMaxWidth(),
                            factory = { context ->
                                MaterialDayPicker(context).apply {
                                    locale = Locale.KOREAN
                                    setDaySelectionChangedListener { days ->
                                        viewModel.onEvent(ManageClassroomEvent.NewClassroomWeekdayChanged(days))
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        MainThemeRoundButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.create_classroom)
                        ) {
                            if(
                                state.value.newClassroomName.isNotBlank() &&
                                state.value.newClassroomDays.isNotEmpty()
                            ) {
                                viewModel.onEvent(ManageClassroomEvent.CreateClassroom)
                            }else{
                                Toast.makeText(context,"입력되지 않은 항목이 존재합니다",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}