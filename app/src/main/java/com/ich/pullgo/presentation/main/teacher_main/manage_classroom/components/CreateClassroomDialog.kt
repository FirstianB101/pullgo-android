package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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

    var spinnerState by remember { mutableStateOf(false) }
    var appliedAcademies: List<Academy> by remember { mutableStateOf(emptyList()) }
    var selectedAcademy: Academy? by remember { mutableStateOf(null) }
    var selectAcademyState by remember { mutableStateOf(false) }

    var classroomName by remember { mutableStateOf("") }
    var selectedDays: List<MaterialDayPicker.Weekday> by remember { mutableStateOf(emptyList())}

    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher

    when (state.value) {
        is ManageClassroomState.Loading -> {
            LoadingScreen()
        }
        is ManageClassroomState.GetAcademies -> {
            appliedAcademies = (state.value as ManageClassroomState.GetAcademies).academies
            viewModel.onResultConsume()
        }
        is ManageClassroomState.CreateClassroom -> {
            showDialog.value = false
        }
    }

    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
                classroomName = ""
                selectedAcademy = null
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
                                value = classroomName,
                                onValueChange = {classroomName = it},
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
                                        selectedDays = days
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        MainThemeRoundButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.create_classroom)
                        ) {
                            if(classroomName.isNotBlank() && selectedDays.isNotEmpty()) {
                                val days = StringBuilder()
                                if (selectedDays.contains(MaterialDayPicker.Weekday.MONDAY)) days.append("월")
                                if (selectedDays.contains(MaterialDayPicker.Weekday.TUESDAY)) days.append("화")
                                if (selectedDays.contains(MaterialDayPicker.Weekday.WEDNESDAY)) days.append("수")
                                if (selectedDays.contains(MaterialDayPicker.Weekday.THURSDAY)) days.append("목")
                                if (selectedDays.contains(MaterialDayPicker.Weekday.FRIDAY)) days.append("금")
                                if (selectedDays.contains(MaterialDayPicker.Weekday.SATURDAY)) days.append("토")
                                if (selectedDays.contains(MaterialDayPicker.Weekday.SUNDAY)) days.append("일")

                                viewModel.createClassroom(
                                    Classroom(
                                        academyId = selectedAcademy?.id!!,
                                        name = "$classroomName;$days",
                                        creatorId = teacher?.id!!
                                    )
                                )
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