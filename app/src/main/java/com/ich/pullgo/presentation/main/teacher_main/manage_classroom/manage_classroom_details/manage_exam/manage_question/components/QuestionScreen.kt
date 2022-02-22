package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.manage_question.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Question

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditableQuestionScreen(
    modifier: Modifier = Modifier,
    question: Question
) {
    var content by remember{ mutableStateOf(question.content ?: "")}
    var pictureUrl by remember { mutableStateOf(question.pictureUrl) }

    LaunchedEffect(question){
        content = question.content!!
        pictureUrl = question.pictureUrl
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri != null) {
            question.pictureUrl = uri.toString()
            pictureUrl = uri.toString()
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        BasicTextField(
            modifier = Modifier
                .height(120.dp)
                .padding(8.dp),
            value = content,
            onValueChange = {
                content = it
                question.content = it
            },
            textStyle = TextStyle(fontSize = 20.sp),
            maxLines = 5,
            decorationBox = { innerTextField ->
                Row{
                    if (question.content.isNullOrBlank()) {
                        Text(
                            text = "문제 입력",
                            fontSize = 20.sp,
                            color = Color.LightGray
                        )
                    }
                    innerTextField()
                }
            },
        )

        Text(
            modifier = Modifier.align(Alignment.End),
            text = stringResource(R.string.comment_click_under_image_for_register),
            color = colorResource(android.R.color.holo_orange_dark)
        )

        val painter = rememberImagePainter(
            data = pictureUrl,
            builder = {size(OriginalSize)}
        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            painter = if(question.pictureUrl.isNullOrBlank()) painterResource(R.drawable.add_picture) else painter,
            contentDescription = "null",
            contentScale = ContentScale.FillWidth
        )
    }
}