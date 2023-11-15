package com.example.newsfeed.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newsfeed.ui.theme.webOrange

@Composable
fun ScrollUpIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
            .padding(bottom = 50.dp, end = 15.dp)
    ) {
        Box(
            modifier = modifier
                .size(40.dp)
                .background(webOrange, shape = RoundedCornerShape(50f))
        ) {
            Icon(
                modifier = modifier
                    .size(40.dp),
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Up",
                tint = Color.White
            )
        }
    }
}