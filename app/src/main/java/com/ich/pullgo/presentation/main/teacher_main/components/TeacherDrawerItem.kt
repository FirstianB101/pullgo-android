package com.ich.pullgo.presentation.main.teacher_main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.presentation.main.common.util.TeacherMainScreens

@Composable
fun TeacherDrawerItem(item: TeacherMainScreens, selected: Boolean, onItemClick: (TeacherMainScreens) -> Unit) {
    val background = if (selected) R.color.main_color else android.R.color.transparent
    val iconColor = if (selected) Color.White else Color.Gray
    val contentColor = if(selected) Color.White else Color.Black

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(48.dp)
            .padding(10.dp, 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = background))
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(iconColor),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = contentColor
        )
    }
}