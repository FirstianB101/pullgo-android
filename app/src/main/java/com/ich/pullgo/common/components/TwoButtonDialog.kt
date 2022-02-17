package com.ich.pullgo.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun TwoButtonDialog(
    title: String,
    content: String,
    dialogState: MutableState<Boolean>,
    cancelText: String,
    confirmText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    MaterialTheme {
        Column {
            if (dialogState.value) {
                AlertDialog(
                    onDismissRequest = {
                       dialogState.value = false
                    },
                    title = {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = { Text(text = content) },
                    confirmButton = {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(com.ich.pullgo.R.color.main_color),
                                backgroundColor = Color.White
                            ),
                            onClick = onConfirm
                        ) {
                            Text(confirmText)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(com.ich.pullgo.R.color.main_color),
                                backgroundColor = Color.White
                            ),
                            onClick = onCancel
                        ) {
                            Text(cancelText)
                        }
                    }
                )
            }
        }

    }
}