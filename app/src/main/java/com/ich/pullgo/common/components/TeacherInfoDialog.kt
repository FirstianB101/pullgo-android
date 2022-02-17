package com.ich.pullgo.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Teacher


@ExperimentalComposeUiApi
@Composable
fun TeacherInfoDialog(
    showDialog: MutableState<Boolean>,
    showRemoveIcon: Boolean,
    teacher: Teacher,
    onRemoveClicked: () -> Unit
){
    if(showDialog.value){
        Dialog(
            onDismissRequest = { showDialog.value = false },
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
                            text = stringResource(R.string.teacher_info),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))

                        if(showRemoveIcon) {
                            IconButton(
                                onClick = onRemoveClicked
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PersonRemove,
                                    contentDescription = null,
                                    tint = colorResource(R.color.material_700_red)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.width(100.dp),
                            text = stringResource(R.string.teacher_name),
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            modifier = Modifier.width(170.dp),
                            text = teacher.account?.fullName ?: "",
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
                            text = stringResource(R.string.user_name),
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            modifier = Modifier.width(170.dp),
                            text = teacher.account?.username ?: "",
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
                            text = stringResource(R.string.phone),
                            color = Color.Black,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            modifier = Modifier.width(170.dp),
                            text = teacher.account?.phone ?: "",
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