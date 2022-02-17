package com.ich.pullgo.presentation.main.common.components.apply_academy_screen.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.domain.model.Academy

@ExperimentalComposeUiApi
@Composable
fun CreateAcademyDialog(
    showDialog: Boolean,
    onCreateClicked: (Academy) -> Unit,
    onCancelClicked: () -> Unit,
){
    val context = LocalContext.current

    var academyName by remember { mutableStateOf("") }
    var academyAddress by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val teacher = PullgoApplication.instance?.getLoginUser()?.teacher

    if(showDialog){
        Dialog(
            onDismissRequest = onCancelClicked,
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.create_academy),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = academyName,
                        onValueChange = {academyName = it},
                        label = { Text(text = stringResource(R.string.academy_name)) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = academyAddress,
                        onValueChange = {academyAddress = it},
                        label = { Text(text = stringResource(R.string.academy_address)) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = phone,
                        onValueChange = {phone = it},
                        label = { Text(text = stringResource(R.string.phone)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        TextButton(
                            onClick = {
                                if(academyName.isNotBlank() && academyAddress.isNotBlank() && phone.isNotBlank()){
                                    onCreateClicked(
                                        Academy(
                                            name = academyName,
                                            address = academyAddress,
                                            phone = phone,
                                            ownerId = teacher?.id!!
                                        )
                                    )
                                }
                                else Toast.makeText(context,"정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show()
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
                            onClick = onCancelClicked
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
    }
}