package com.ich.pullgo.common.components.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.Teacher
import com.ich.pullgo.presentation.login.ui.theme.PullgoTheme

@Composable
fun TeacherItem(
    modifier: Modifier = Modifier,
    teacher: Teacher,
    showDeleteButton: Boolean,
    showAcceptButton: Boolean,
    onDeleteButtonClicked: () -> Unit,
    onAcceptButtonClicked: () -> Unit
){
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
                    text = "${teacher.account?.fullName}",
                    color = Color.Black,
                    fontSize = 19.sp,
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

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${teacher.account?.username}",
                    color = colorResource(R.color.secondary_color),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if(showAcceptButton){
                    TextButton(
                        onClick = onAcceptButtonClicked
                    ) {
                        Text(
                            text = stringResource(R.string.underlined_accept_apply_academy),
                            color = colorResource(R.color.secondary_color),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherItemPreview() {
    PullgoTheme {
        TeacherItem(
            teacher = Teacher(Account("아이디","이름","전화번호",null)),
            showDeleteButton = true,
            showAcceptButton = true,
            onDeleteButtonClicked = {}
        ) {}
    }
}