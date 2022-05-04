package com.ich.pullgo.presentation.main.student_main.exam_history_screen.exam_review.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.ich.pullgo.domain.model.Question

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UnEditableQuestionScreen(
    modifier: Modifier = Modifier,
    question: Question,
    questionNum: Int,
    isCorrect: Boolean?
) {
    val scrollState = rememberScrollState()

    when(isCorrect){
        true -> {
            Box(modifier = Modifier
                .size(40.dp)
                .padding(4.dp)
                .border(BorderStroke(2.dp, Color.Red), shape = CircleShape)
            )
        }
        false -> {
            Box(modifier = Modifier
                .size(40.dp)
                .padding(4.dp)){
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Close,
                    contentDescription = "",
                    tint = Color.Red
                )
            }
        }
        null -> {}
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        BasicTextField(
            modifier = Modifier
                .height(120.dp)
                .padding(8.dp),
            value = "$questionNum. ${question.content}",
            readOnly = true,
            onValueChange = {},
            textStyle = TextStyle(fontSize = 20.sp),
            maxLines = 5
        )

        val painter = rememberImagePainter(
            data = question.pictureUrl,
            builder = {size(OriginalSize)}
        )

        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painter,
            contentDescription = "null",
            contentScale = ContentScale.FillWidth
        )
    }
}