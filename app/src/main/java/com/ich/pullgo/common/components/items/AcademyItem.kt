package com.ich.pullgo.common.components.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ich.pullgo.domain.model.Academy
import com.ich.pullgo.ui.theme.PullgoTheme

@Composable
fun AcademyItem(
    modifier: Modifier = Modifier,
    academy: Academy,
    showDeleteButton: Boolean,
    onDeleteButtonClicked: () -> Unit 
){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Row (verticalAlignment = Alignment.Top){
                Text(
                    modifier = Modifier.weight(1f),
                    text = academy.name.toString(),
                    color = Color.Black,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                if(showDeleteButton){
                    IconButton(
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        onClick = onDeleteButtonClicked
                    ) {
                        Icon(Icons.Filled.Clear,"")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = academy.address.toString(),
                color = Color.DarkGray,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AcademyItemPreview() {
    PullgoTheme {
        val academy = Academy("name","010-xxxx-xxxx","주소",null)
        AcademyItem(
            academy = academy,
            showDeleteButton = true
        ) {

        }
    }
}