package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CircleBackgroundText(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    fontSize: TextUnit
){
    Box(contentAlignment= Alignment.Center,
        modifier = Modifier
            .background(backgroundColor, shape = CircleShape)
            .layout{ measurable, constraints ->
                val placeable = measurable.measure(constraints)

                val currentHeight = placeable.height
                var heightCircle = currentHeight
                if (placeable.width > heightCircle)
                    heightCircle = placeable.width

                layout(heightCircle, heightCircle) {
                    placeable.placeRelative(0, (heightCircle-currentHeight)/2)
                }
            }) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier.padding(4.dp).defaultMinSize(48.dp),
            fontSize = fontSize
        )
    }
}