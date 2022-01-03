package com.ich.pullgo.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OneButtonDialog(
    title: String,
    content: String,
    buttonText: String,
    dialogState: MutableState<Boolean>,
    buttonClick: () -> Unit
) {
    MaterialTheme {
        Column (){
            if (dialogState.value) {
                AlertDialog(
                    onDismissRequest = {},
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
                            onClick = buttonClick
                        ) {
                            Text(buttonText)
                        }
                    },
                )
            }
        }

    }
}