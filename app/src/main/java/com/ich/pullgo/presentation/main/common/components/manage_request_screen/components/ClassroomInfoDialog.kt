package com.ich.pullgo.presentation.main.common.components.manage_request_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestState
import com.ich.pullgo.presentation.main.common.components.manage_request_screen.ManageRequestViewModel
import com.ich.pullgo.R

@ExperimentalComposeUiApi
@Composable
fun ClassroomInfoDialog(
    showDialog: MutableState<Boolean>,
    classroom: Classroom?,
    viewModel: ManageRequestViewModel = hiltViewModel()
){
    if(showDialog.value){
        val state = viewModel.state.collectAsState()
        val classroomInfos = classroom?.name?.split(';')!!

        Dialog(
            onDismissRequest = {showDialog.value = false},
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
                        .fillMaxWidth()
                        .padding(25.dp)
                        .background(
                            Color.White, RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_info_24),
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                        Text(
                            text = stringResource(R.string.classroom_info),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.width(100.dp),
                            text = stringResource(R.string.classroom_name),
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            modifier = Modifier.width(170.dp),
                            text = classroomInfos[0],
                            color = Color.Gray,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.width(100.dp),
                            text = "반 담임",
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            modifier = Modifier.width(170.dp),
                            text = classroom.creator?.account?.fullName ?: "",
                            color = Color.Gray,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.width(100.dp),
                            text = "소속 학원",
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            modifier = Modifier.width(170.dp),
                            text = state.value.academyInfo?.name ?: "",
                            color = Color.Gray,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {showDialog.value = false}
                    ) {
                        Text(
                            text = stringResource(R.string.confirm),
                            color = colorResource(R.color.main_color),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}