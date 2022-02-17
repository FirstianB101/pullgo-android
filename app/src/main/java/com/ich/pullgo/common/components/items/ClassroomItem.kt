package com.ich.pullgo.common.components.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Classroom
import com.ich.pullgo.presentation.theme.PullgoTheme

@Composable
fun ClassroomItem(
    modifier: Modifier = Modifier,
    classroom: Classroom,
    showDeleteButton: Boolean,
    onDeleteButtonClicked: () -> Unit
) {
    val classroomInfos = classroom.name?.split(';')!!

    val classroomName = classroomInfos[0]
    val classroomDate = classroomInfos[1]

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${classroom.creator?.account?.fullName} 선생님",
                    color = colorResource(R.color.secondary_color),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                if (showDeleteButton) {
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = onDeleteButtonClicked
                    ) {
                        Icon(Icons.Filled.Clear, "")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = classroomDate,
                color = Color.Gray,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = classroomName,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClassroomItemPreview() {
    PullgoTheme {
        ClassroomItem(
            classroom = Classroom(null,"반 이름;월수",null),
            showDeleteButton = true
        ) {
        }
    }
}