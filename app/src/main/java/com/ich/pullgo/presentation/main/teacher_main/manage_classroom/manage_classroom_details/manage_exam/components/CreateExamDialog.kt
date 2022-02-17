package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ich.pullgo.PullgoApplication
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Exam
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.Duration

@ExperimentalComposeUiApi
@Composable
fun CreateExamDialog(
    selectedClassroomId: Long,
    showDialog: MutableState<Boolean>,
    onCreateClick: (Exam) -> Unit
){
    val context = LocalContext.current

    var examName by remember { mutableStateOf("") }
    var beginDate by remember { mutableStateOf("") }
    var beginTime by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var timeLimit by remember { mutableStateOf("") }
    var passScore by remember { mutableStateOf("") }

    val beginDateDialogState = rememberMaterialDialogState()
    val beginTimeDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher

    fun resetDialog(){
        examName = ""
        beginDate = ""
        beginTime = ""
        endDate = ""
        endTime = ""
        timeLimit = ""
        passScore = ""
    }

    if(showDialog.value){
        Dialog(
            onDismissRequest = {
                showDialog.value = false
                resetDialog()
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
                shape = RoundedCornerShape(8.dp)
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
                            text = stringResource(R.string.exam_name),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.width(260.dp),
                            value = examName,
                            onValueChange = {examName = it},
                            label = { Text(text = stringResource(R.string.comment_input_exam_name)) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = stringResource(R.string.exam_begin_date),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            readOnly = true,
                            modifier = Modifier.width(260.dp),
                            value = beginDate,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.comment_select_begin_date)) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        beginDateDialogState.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = "",
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            readOnly = true,
                            modifier = Modifier.width(260.dp),
                            value = beginTime,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.exam_begin_time)) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        beginTimeDialogState.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = stringResource(R.string.exam_end_date),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            readOnly = true,
                            modifier = Modifier.width(260.dp),
                            value = endDate,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.comment_select_end_date)) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        endDateDialogState.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = "",
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            readOnly = true,
                            modifier = Modifier.width(260.dp),
                            value = endTime,
                            onValueChange = {},
                            label = { Text(text = stringResource(R.string.comment_select_end_time)) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        endTimeDialogState.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = stringResource(R.string.exam_time_limit),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.width(260.dp),
                            value = timeLimit,
                            onValueChange = {timeLimit = it},
                            label = { Text(text = stringResource(R.string.comment_input_exam_time_limit)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(80.dp),
                            text = stringResource(R.string.exam_pass_score),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        OutlinedTextField(
                            modifier = Modifier.width(260.dp),
                            value = passScore,
                            onValueChange = {passScore = it},
                            label = { Text(text = stringResource(R.string.comment_input_exam_pass_score)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(
                            onClick = {
                                if(examName.isNotBlank() && beginDate.isNotBlank() && beginTime.isNotBlank() && endDate.isNotBlank() && endTime.isNotBlank()
                                    && passScore.isNotBlank() && timeLimit.isNotBlank()){
                                    val newExam = Exam(
                                        classroomId = selectedClassroomId,
                                        creatorId = teacher?.id!!,
                                        name = examName,
                                        beginDateTime = "${beginDate}T${beginTime}:00",
                                        endDateTime = "${endDate}T${endTime}:00",
                                        timeLimit = Duration.ofMinutes(timeLimit.toLong()).toString(),
                                        cancelled = false,
                                        finished = false,
                                        passScore = passScore.toInt()
                                    )
                                    onCreateClick(newExam)
                                    resetDialog()
                                }else{
                                    Toast.makeText(context,"정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.create),
                                color = colorResource(R.color.secondary_color),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))

                        TextButton(
                            onClick = {
                                showDialog.value = false
                                resetDialog()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = colorResource(R.color.secondary_color),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        MaterialDialog(
            dialogState = beginDateDialogState,
            buttons = {
                positiveButton("OK")
                negativeButton("Cancel")
            }
        ) {
            datepicker { date ->
                beginDate = date.toString()
            }
        }

        MaterialDialog(
            dialogState = beginTimeDialogState,
            buttons = {
                positiveButton("OK")
                negativeButton("Cancel")
            }
        ) {
            timepicker { time ->
                beginTime = time.toString()
            }
        }

        MaterialDialog(
            dialogState = endDateDialogState,
            buttons = {
                positiveButton("OK")
                negativeButton("Cancel")
            }
        ) {
            datepicker { date ->
                endDate = date.toString()
            }
        }

        MaterialDialog(
            dialogState = endTimeDialogState,
            buttons = {
                positiveButton("OK")
                negativeButton("Cancel")
            }
        ) {
            timepicker { time ->
                endTime = time.toString()
            }
        }
    }
}