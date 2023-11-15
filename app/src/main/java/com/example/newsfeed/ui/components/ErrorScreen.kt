package com.example.newsfeed.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsfeed.R
import com.example.newsfeed.ui.theme.chestnut_84
import com.example.newsfeed.ui.theme.poppinsFontFamily

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = chestnut_84),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.information_circle),
                contentDescription = "error icon"
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                text = errorMessage,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 13.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}