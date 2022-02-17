package com.ich.pullgo.common.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import com.ich.pullgo.presentation.theme.PullgoTheme

@Composable
fun ManageTeacherItem(
    modifier: Modifier = Modifier,
    teacher: Teacher,
    showKickButton: Boolean,
    onKickButtonClicked: () -> Unit,
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${teacher.account?.fullName}",
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${teacher.account?.username}",
                    color = colorResource(R.color.secondary_color),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                if(showKickButton){
                    TextButton(
                        modifier = Modifier,
                        onClick = onKickButtonClicked
                    ) {
                        Text(
                            text = stringResource(R.string.underlined_kick_people),
                            color = colorResource(R.color.material_700_red),
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageTeacherItemPreview() {
    PullgoTheme {
        ManageTeacherItem(
            teacher = Teacher(Account("아이디","이름","전화번호",null)),
            showKickButton = true
        ) {

        }
    }
}