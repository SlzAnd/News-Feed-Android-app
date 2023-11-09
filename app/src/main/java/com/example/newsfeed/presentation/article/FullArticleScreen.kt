package com.example.newsfeed.presentation.article

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newsfeed.R
import com.example.newsfeed.presentation.components.BottomNavigationBar
import com.example.newsfeed.presentation.components.CustomTopAppBar
import com.example.newsfeed.ui.theme.white_opacity_89
import com.kevinnzou.accompanist.web.WebView
import com.kevinnzou.accompanist.web.rememberWebViewState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullArticleScreen(
    navController: NavHostController,
    viewModel: ArticleViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val webViewState = rememberWebViewState(url = state.url)

    val infiniteTransition = rememberInfiniteTransition("rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        ), label = ""
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                hasBookmarkIcon = true,
                isBookmarked = state.isBookmarked,
                onBookmarkEvent = { viewModel.onBookmarkClick() }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        WebView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = webViewState
        )

        if (webViewState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = white_opacity_89),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer { rotationZ = angle },
                    painter = painterResource(id = R.drawable.radial_progress),
                    contentDescription = "loading icon"
                )
            }
        }
    }
}