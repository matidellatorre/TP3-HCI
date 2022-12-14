package com.example.tp3_hci.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.example.tp3_hci.navigation.MyAppNavHost

@Composable
fun CicleEntry (
    title: String,
    exerciseCount : Int,
    rounds: Int,
    onNavigateToCycleDetails: (id:Int) -> Unit
    ) {
    Card(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 4.dp)
        .clickable { onNavigateToCycleDetails(1) },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 5.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp
                )
                Text(
                    text = "$exerciseCount Exercises",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$rounds Rounds",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
            Column (
                horizontalAlignment = Alignment.End,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(18.dp),
                )
            }
        }
    }
}