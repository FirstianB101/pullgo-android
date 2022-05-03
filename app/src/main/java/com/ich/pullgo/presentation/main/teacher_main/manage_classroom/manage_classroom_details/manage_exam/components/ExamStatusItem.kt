package com.ich.pullgo.presentation.main.teacher_main.manage_classroom.manage_classroom_details.manage_exam.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.R
import com.ich.pullgo.common.util.Constants
import com.ich.pullgo.domain.model.Account
import com.ich.pullgo.domain.model.AttenderState
import com.ich.pullgo.domain.model.Student
import com.ich.pullgo.presentation.theme.PullgoTheme

@Composable
fun ExamStatusItem(
    modifier: Modifier = Modifier,
    student: Student?,
    attenderState: AttenderState?
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10),
        elevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "${student?.account?.fullName}",
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = "${student?.schoolName}",
                        fontSize = 17.sp,
                        color = colorResource(R.color.secondary_color) ,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${student?.schoolYear}학년",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = if(attenderState?.progress == Constants.ATTENDER_PROGRESS_COMPLETE)
                        "${attenderState.score} / 100 점" else "",
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }

            when(attenderState?.progress){
                Constants.ATTENDER_PROGRESS_COMPLETE -> {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20))
                            .background(colorResource(R.color.success_green)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "응시 완료",
                            fontSize = 20.sp
                        )
                    }
                }
                Constants.ATTENDER_PROGRESS_ONGOING -> {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20))
                            .background(colorResource(R.color.ongoing_orange)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "응시중",
                            fontSize = 20.sp
                        )
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "미응시",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExamStatusItemPreview() {
    PullgoTheme {
        ExamStatusItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            student = Student(
                Account("username","학생 이름","",""),
                "",
                "무슨 학교",
                3
            ),
            attenderState = AttenderState(null,null,"ONGOING",80)
        )
    }
}