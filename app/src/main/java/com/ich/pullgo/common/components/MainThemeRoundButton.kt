package com.ich.pullgo.common.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R

@Composable
fun MainThemeRoundButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        content = { Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp
        )
        },
        onClick = onClick,
        shape = RoundedCornerShape(40),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.main_color)),
        contentPadding = PaddingValues(14.dp)
    )
}