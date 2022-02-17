package com.ich.pullgo.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun MultiToggleButton(
    modifier: Modifier,
    currentSelection: String,
    toggleStates: List<String>,
    onToggleChange: (String) -> Unit
) {
    val selectedTint = colorResource(com.ich.pullgo.R.color.main_color)
    val unselectedTint = Color.Unspecified

    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .border(BorderStroke(1.dp, Color.LightGray))) {
        toggleStates.forEachIndexed { index, toggleState ->
            val isSelected = currentSelection.lowercase() == toggleState.lowercase()
            val backgroundTint = if (isSelected) selectedTint else unselectedTint
            val textColor = if (isSelected) Color.White else Color.Unspecified

            if (index != 0) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(backgroundTint)
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .weight(1f)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChange(toggleState)
                            }
                        }),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(toggleState, color = textColor, modifier = Modifier.padding(4.dp))
            }

        }
    }
}