package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.edit_classroom.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.common.components.MainThemeRoundButton
import com.ich.pullgo.domain.model.Classroom
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun EditClassroomDialog(
    showDialog: MutableState<Boolean>,
    selectedClassroom: Classroom,
    onEditClicked: (Classroom) -> Unit
) {
    val context = LocalContext.current

    val infos = selectedClassroom.name?.split(';')

    var classroomName by remember { mutableStateOf(infos?.get(0).toString()) }

    var selectedDays: List<MaterialDayPicker.Weekday> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit){
        val dayList = mutableListOf<MaterialDayPicker.Weekday>()
        infos?.get(1)?.forEach { day ->
            when(day){
                '월' -> dayList.add(MaterialDayPicker.Weekday.MONDAY)
                '화' -> dayList.add(MaterialDayPicker.Weekday.TUESDAY)
                '수' -> dayList.add(MaterialDayPicker.Weekday.WEDNESDAY)
                '목' -> dayList.add(MaterialDayPicker.Weekday.THURSDAY)
                '금' -> dayList.add(MaterialDayPicker.Weekday.FRIDAY)
                '토' -> dayList.add(MaterialDayPicker.Weekday.SATURDAY)
                '일' -> dayList.add(MaterialDayPicker.Weekday.SUNDAY)
            }
        }
        selectedDays = dayList
    }

    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher

    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
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
                            text = stringResource(R.string.classroom_name),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.width(260.dp),
                            value = classroomName,
                            onValueChange = { classroomName = it },
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
                                setSelectedDays(selectedDays)
                                setDaySelectionChangedListener { days ->
                                    selectedDays = days
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    MainThemeRoundButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.edit)
                    ) {
                        if (classroomName.isNotBlank() && selectedDays.isNotEmpty()) {
                            val days = StringBuilder()
                            if (selectedDays.contains(MaterialDayPicker.Weekday.MONDAY)) days.append("월")
                            if (selectedDays.contains(MaterialDayPicker.Weekday.TUESDAY)) days.append("화")
                            if (selectedDays.contains(MaterialDayPicker.Weekday.WEDNESDAY)) days.append("수")
                            if (selectedDays.contains(MaterialDayPicker.Weekday.THURSDAY)) days.append("목")
                            if (selectedDays.contains(MaterialDayPicker.Weekday.FRIDAY)) days.append("금")
                            if (selectedDays.contains(MaterialDayPicker.Weekday.SATURDAY)) days.append("토")
                            if (selectedDays.contains(MaterialDayPicker.Weekday.SUNDAY)) days.append("일")

                            val edited = Classroom(
                                academyId = selectedClassroom.academyId,
                                name = "$classroomName;$days",
                                creatorId = selectedClassroom.creatorId
                            )

                            onEditClicked(edited)
                        } else {
                            Toast.makeText(context, "입력되지 않은 항목이 존재합니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}