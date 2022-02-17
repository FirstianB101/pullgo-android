package com.ich.pullgo.presentation.main.common.components.calendar_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ich.pullgo.domain.model.Lesson

@ExperimentalComposeUiApi
@Composable
fun StudentLessonInfoDialog(
    showDialog: Boolean,
    lesson: Lesson,
    onClose: () -> Unit
){
    if(showDialog){
        Dialog(
            onDismissRequest = onClose,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            ),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    StudentLessonInfoScreen(
                        lesson = lesson,
                        onButtonClick = onClose
                    )
                }
            }
        }
    }
}